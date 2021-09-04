package com.soten.memo.ui.memoedit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.soten.memo.BuildConfig
import com.soten.memo.MemoApplication.Companion.appContext
import com.soten.memo.R
import com.soten.memo.adapter.PhotoAdapter
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.FragmentMemoEditBinding
import com.soten.memo.ui.MemoSharedViewModel
import com.soten.memo.util.CameraUtil
import com.soten.memo.util.PathUtil
import com.soten.memo.util.addImage
import com.soten.memo.util.removeImage
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MemoEditFragment : Fragment() {

    private var _binding: FragmentMemoEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel by sharedViewModel<MemoSharedViewModel>()

    private var mediaState = MediaState.GALLERY

    private val photoAdapter by lazy {
        PhotoAdapter { path: String -> removePhoto(path) }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            if (mediaState == MediaState.GALLERY) {
                val galleryUri = result.data?.data as Uri
                createImageFile()
                val filePath = PathUtil.getPath(requireContext(), galleryUri)
                val newFile = File(currentPhotoPath)
                FileOutputStream(newFile).apply {
                    this.write(File(filePath!!).readBytes())
                    this.close()
                }
                viewModel.imagePathLiveData.addImage(currentPhotoPath)
            }

            if (mediaState == MediaState.CAMERA) {
                try {
                    val file = File(currentPhotoPath)
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver, Uri.fromFile(file)
                    )
                    val exif = ExifInterface(currentPhotoPath)
                    val exifOrient: Int = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    val exifDegree = CameraUtil.exifOrientationToDegrees(exifOrient)
                    bitmap = CameraUtil.rotate(bitmap, exifDegree)
                    getImageUri(appContext, bitmap) // 찍은 사진 앨범 저장 기능

                    viewModel.imagePathLiveData.addImage(currentPhotoPath)
                } catch (e: Exception) {
                    Log.d(TAG, e.message!!)
                }
            }

            photoAdapter.setImages(viewModel.imagePathLiveData.value ?: listOf())
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMemoEditBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback {
            Toast.makeText(appContext, "submit 버튼을 눌러주세요", Toast.LENGTH_SHORT).show()
        }

        setFragmentResultListener("requestKey") { _, bundle ->
            val urlLink = bundle.getString("requestKey")

            var imageUri: Uri?
            var path: String? = ""
            var isFail = false

            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBar.visibility = View.VISIBLE
                withContext(Dispatchers.IO) {
                    Glide.with(appContext!!)
                        .asBitmap()
                        .load(Uri.parse(urlLink))
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?,
                            ) {
                                imageUri = getImageUri(appContext, resource)
                                path = PathUtil.getPath(appContext!!, imageUri!!)

                                path?.let {
                                    viewModel.imagePathLiveData.addImage(it)
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                isFail = true
                                Toast.makeText(appContext, "url 받아오기 실패", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                delay(1000L)

                if (isFail) {
                    return@launch
                }
                binding.progressBar.visibility = View.GONE
                photoAdapter.setImages(viewModel.imagePathLiveData.value!!)

                withContext(Dispatchers.IO) {
                    createImageFile()
                    val newFile = File(currentPhotoPath)
                    FileOutputStream(newFile).apply {
                        this.write(File(path!!).readBytes())
                        this.close()
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addMediaSource()

        viewModel.memoStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MemoState.WRITE -> initView()
                is MemoState.MODIFY -> handleModify()
                is MemoState.NORMAL -> handleNormal()
                is MemoState.SUCCESS -> handleSuccess()
                else -> Unit
            }
        }
    }

    private fun addMediaSource() {
        binding.bottomSheet.editImageRecyclerView.adapter = photoAdapter

        binding.bottomSheet.galleryButton.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            mediaState = MediaState.GALLERY
            launcher.launch(intent)
        }

        binding.bottomSheet.cameraButton.setOnClickListener {
            mediaState = MediaState.CAMERA
            takePictureIntent()
        }

        binding.bottomSheet.urlButton.setOnClickListener {
            AddUrlFragment().show(parentFragmentManager, "link")
        }
    }

    private fun initView() {
        binding.submitButton.setOnClickListener {
            handleInsertMemo()
            viewModel.setNormalState()
        }
    }

    // 수정 모드
    private fun handleModify() {
        val memoEntity = viewModel.memoEntityLiveData.value

        try {
            photoAdapter.setImages(memoEntity?.images ?: listOf())
        } catch (exception: Exception) {
            Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
        }

        binding.editMemoTitleText.setText(memoEntity?.title)
        binding.editMemoDescriptionText.setText(memoEntity?.description)

        binding.submitButton.setOnClickListener {
            val updateAt = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date())

            val memoLiveData = MemoEntity(
                id = memoEntity?.id,
                title = binding.editMemoTitleText.text.toString(),
                description = binding.editMemoDescriptionText.text.toString(),
                createdAt = memoEntity!!.createdAt,
                updatedAt = updateAt,
                images = viewModel.imagePathLiveData.value ?: listOf()
            )
            viewModel.setMemoEntity(memoLiveData)
            viewModel.updateMemo(memoLiveData)
            Log.d(TAG, memoLiveData.toString())
            viewModel.setSuccess()
        }
    }

    private fun handleSuccess() {
        findNavController().navigateUp()
    }

    private fun handleInsertMemo() {
        val title = binding.editMemoTitleText.text.toString()
        val description = binding.editMemoDescriptionText.text.toString()
        val updateAt = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date())

        viewModel.insertMemo(
            MemoEntity(
                title = title,
                description = description,
                createdAt = updateAt,
                updatedAt = updateAt,
                images = viewModel.imagePathLiveData.value ?: listOf()
            )
        )
    }

    private fun handleNormal() {
        hideKeyboard()
        findNavController().navigate(R.id.to_memoListFragment)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun removePhoto(path: String) {
        viewModel.imagePathLiveData.removeImage(path)
        val file = File(path)
        file.delete()
        photoAdapter.setImages(viewModel.imagePathLiveData.value ?: listOf())
    }

    private lateinit var currentPhotoPath: String
    private lateinit var currentPhotoName: String

    @Throws(Exception::class)
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        currentPhotoName = "${timeStamp}_.jpg"
        return File.createTempFile(
            "${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {

            }
            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(
                    appContext!!,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                launcher.launch(takePictureIntent)
            }
        }
    }


    private fun getImageUri(context: Context?, bitmap: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val urlPath = MediaStore.Images.Media.insertImage(context?.contentResolver,
            bitmap,
            timeStamp,
            null)
        return Uri.parse(urlPath)
    }

    companion object {
        private const val TAG = "MemoEditFragment"
    }

}

