package com.soten.memo.ui.memodetail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import com.soten.memo.R
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



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")

        binding.detailTitleText.apply {
            movementMethod = ScrollingMovementMethod()
            text = title
        }

        binding.detailDescriptionText.apply {
            movementMethod = ScrollingMovementMethod()
            text = description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

}