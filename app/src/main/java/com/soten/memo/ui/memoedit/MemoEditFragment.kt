package com.soten.memo.ui.memoedit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soten.memo.R
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.FragmentMemoEditBinding
import com.soten.memo.ui.MemoSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class MemoEditFragment : Fragment() {

    private var _binding: FragmentMemoEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel by sharedViewModel<MemoSharedViewModel>()

    private var memoId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoEditBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        viewModel.memoStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MemoState.WRITE -> initView()
                is MemoState.MODIFY -> handleModify(state.memoEntity)
                is MemoState.NORMAL -> handleNormal()
                is MemoState.SUCCESS -> handleSuccess(state.memoEntity)
            }
        }
    }

    // 수정 모드
    private fun handleModify(memoEntity: MemoEntity) {
        binding.editMemoTitleText.setText(memoEntity.title)
        binding.editMemoDescriptionText.setText(memoEntity.description)
        memoId = memoEntity.id!!
        val updateAt = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Date())

        binding.submitButton.setOnClickListener {
            val memo = MemoEntity(
                id = memoId,
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

    private fun initView() {
        binding.submitButton.setOnClickListener {
            viewModel.setNormalState()
        }
    }

    private fun handleNormal() {
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

        findNavController().navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit, menu)
    }


}