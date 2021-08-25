package com.soten.memo.ui.memolist

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soten.memo.R
import com.soten.memo.adapter.MemoAdapter
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.FragmentMemoListBinding
import com.soten.memo.ui.MemoSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MemoListFragment : Fragment() {

    private var _binding: FragmentMemoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by sharedViewModel<MemoSharedViewModel>()

    private lateinit var adapter: MemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindViews()

        observeData()
    }

    private fun initView() {
        binding.memoEditButton.setOnClickListener {
            viewModel.setWriteSate()
        }
    }

    private fun bindViews() {
        adapter = MemoAdapter { memoEntity ->
            viewModel.setReadState(memoEntity)
        }
        binding.memoListRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.memoListRecyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.memoListLiveData.observe(viewLifecycleOwner) {
            (binding.memoListRecyclerView.adapter as MemoAdapter).setMemo(it)
        }

        viewModel.memoStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MemoState.NORMAL -> {
                    initView()
                    bindViews()
                }
                is MemoState.WRITE -> handleWriteState()
                is MemoState.READ -> handleReadState()
                is MemoState.SUCCESS -> {
                    viewModel.setNormalState()
                }
            }
        }
    }

    private fun handleWriteState() {
        findNavController().navigate(R.id.toMemoWriteFragment)
    }

    private fun handleReadState() {
        findNavController().navigate(R.id.toMemoDetailFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.all_delete -> {
                viewModel.deleteAll()
                true
            }
            else -> false
        }

}
