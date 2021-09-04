package com.soten.memo.ui.memodetail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soten.memo.R
import com.soten.memo.adapter.PhotoAdapter
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.FragmentMemoDetailBinding
import com.soten.memo.ui.MemoSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemoDetailFragment : Fragment() {

    private val viewModel by sharedViewModel<MemoSharedViewModel>()

    private var _binding: FragmentMemoDetailBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { PhotoAdapter { _ -> } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMemoDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback {
            viewModel.setNormalState()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    private fun observeData() {
        viewModel.memoStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MemoState.READ -> {
                    handleReadState()
                }
                is MemoState.MODIFY -> {
                    handleModifyState()
                }
                else -> findNavController().navigateUp()
            }
        }
    }

    private fun handleReadState() {
        val memoEntity = viewModel.memoEntityLiveData.value
        Log.d(TAG, memoEntity.toString())
        binding.detailTitleText.apply {
            movementMethod = ScrollingMovementMethod()
            text = memoEntity?.title
        }

        binding.detailDescriptionText.apply {
            movementMethod = ScrollingMovementMethod()
            text = memoEntity?.description
        }

        binding.memoDetailImagesRecyclerView.adapter = adapter
        binding.memoDetailImagesRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        adapter.memoState = MemoState.READ
        adapter.setImages(memoEntity?.images ?: listOf())
    }

    private fun handleModifyState() {
        viewModel.setMemoEntity(
            MemoEntity(
                id = viewModel.memoEntityLiveData.value?.id,
                title = binding.detailTitleText.text.toString(),
                description = binding.detailDescriptionText.text.toString(),
                createdAt = viewModel.memoEntityLiveData.value?.createdAt!!,
                images = viewModel.memoEntityLiveData.value?.images ?: mutableListOf()
            )
        )
        findNavController().navigate(R.id.toMemoEditFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.delete(viewModel.memoEntityLiveData.value!!)
                viewModel.setNormalState()
                true
            }
            R.id.menu_modify -> {
                viewModel.setModifySate()
                true
            }
            else -> true
        }

    companion object {
        private const val TAG = "MemoDetail"
    }

}

