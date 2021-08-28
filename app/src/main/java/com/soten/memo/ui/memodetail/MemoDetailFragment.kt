package com.soten.memo.ui.memodetail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soten.memo.R
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.FragmentMemoDetailBinding
import com.soten.memo.ui.MemoSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemoDetailFragment : Fragment() {

    private val viewModel by sharedViewModel<MemoSharedViewModel>()

    private var _binding: FragmentMemoDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
                else -> findNavController().navigateUp().run {
                    viewModel.setNormalState()
                }
            }
        }
    }

    private fun handleReadState() {
        val memoEntity = viewModel.memoEntityLiveData.value
        binding.detailTitleText.apply {
            movementMethod = ScrollingMovementMethod()
            text = memoEntity?.title
        }

        binding.detailDescriptionText.apply {
            movementMethod = ScrollingMovementMethod()
            text = memoEntity?.description
        }
    }

    private fun handleModifyState() {
        viewModel.setMemoEntity(
            MemoEntity(
                id = viewModel.memoEntityLiveData.value?.id,
                title = binding.detailTitleText.text.toString(),
                description = binding.detailDescriptionText.text.toString(),
                createdAt = viewModel.memoEntityLiveData.value?.createdAt!!,
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
                true
            }
            R.id.menu_modify -> {
                viewModel.setModifySate()
                true
            }
            else -> false
        }
}

