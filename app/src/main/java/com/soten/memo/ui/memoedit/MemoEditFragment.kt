package com.soten.memo.ui.memoedit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.soten.memo.adapter.PhotoAdapter
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.FragmentMemoEditBinding
import com.soten.memo.ui.MemoSharedViewModel
import com.soten.memo.util.UriUtil
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class MemoEditFragment : Fragment() {

    private var _binding: FragmentMemoEditBinding? = null
    private val binding get() = _binding!!

    private var urlList = mutableListOf<Uri>()

    private val viewModel by sharedViewModel<MemoSharedViewModel>()

    private var mediaState = MediaState.GALLERY

    private val adapter = PhotoAdapter()

    private val launch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            if (mediaState == MediaState.GALLERY) {
                val galleryUrl = result.data?.data as Uri
                urlList.add(galleryUrl)
            }

            if (mediaState == MediaState.CAMERA) {
                val cameraUrl = result.data?.extras?.get("data") as Bitmap
                urlList.add(UriUtil.getImageUri(requireContext(), cameraUrl)!!)
            }

            if (mediaState == MediaState.LINK) {
                val urlLink = result.data?.getStringExtra("url")
                urlList.add(Uri.parse(urlLink))
            }

            adapter.setImages(urlList)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoEditBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback {
            viewModel.setNormalState()
        }

        setFragmentResultListener("requestKey") { _, bundle ->
            val text = bundle.getString("requestKey")
            urlList.add(Uri.parse(text))

            mediaState = MediaState.LINK

            adapter.setImages(urlList)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addMediaSource()

        viewModel.memoStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MemoState.WRITE -> initView()
                is MemoState.MODIFY -> handleModify(state.memoEntity)
                is MemoState.NORMAL -> handleNormal()
                is MemoState.SUCCESS -> handleSuccess(state.memoEntity)
                else -> Unit
            }
        }
    }

    private fun addMediaSource() {
        binding.bottomSheet.editImageRecyclerView.adapter = adapter
        binding.bottomSheet.galleryButton.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            mediaState = MediaState.GALLERY
            launch.launch(intent)
        }

        binding.bottomSheet.cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            mediaState = MediaState.CAMERA
            launch.launch(intent)
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
    private fun handleModify(memoEntity: MemoEntity) {
        binding.editMemoTitleText.setText(memoEntity.title)
        binding.editMemoDescriptionText.setText(memoEntity.description)

        binding.submitButton.setOnClickListener {
            val updateAt = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date())
            val memo = MemoEntity(
                id = memoEntity.id,
                title = binding.editMemoTitleText.text.toString(),
                description = binding.editMemoDescriptionText.text.toString(),
                createdAt = memoEntity.createdAt,
                updatedAt = updateAt
            )
            viewModel.setSuccess(memo)
        }
    }

    private fun handleSuccess(memoEntity: MemoEntity) {
        viewModel.updateMemo(memoEntity)
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
                updatedAt = updateAt
            )
        )
    }

    private fun handleNormal() {
        hideKeyboard()
        findNavController().navigateUp()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

}