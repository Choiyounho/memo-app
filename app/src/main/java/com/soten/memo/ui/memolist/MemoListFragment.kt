package com.soten.memo.ui.memolist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soten.memo.R
import com.soten.memo.adapter.MemoAdapter
import com.soten.memo.databinding.FragmentMemoListBinding
import kotlinx.coroutines.MainScope
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.scope.Scope

class MemoListFragment : Fragment() {


    private var _binding: FragmentMemoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by sharedViewModel<MemoListViewModel>()

    private lateinit var adapter: MemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.memoEditButton.setOnClickListener {
            findNavController().navigate(R.id.toMemoWriteFragment)
        }

        adapter = MemoAdapter()
        binding.memoListRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.memoListRecyclerView.adapter = adapter

        viewModel.memoListLiveData.observe(viewLifecycleOwner) {
            (binding.memoListRecyclerView.adapter as MemoAdapter).setMemo(it)
        }

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
                Log.d("TestT", "전체 삭제")
                viewModel.deleteAll()
                true
            }
            else -> false
        }
    }
