package com.soten.memo.ui.memodetail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.soten.memo.R
import com.soten.memo.databinding.FragmentMemoDetailBinding

class MemoDetailFragment : Fragment() {

    private var _binding: FragmentMemoDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        binding.detailTitleText.movementMethod = ScrollingMovementMethod()
        binding.detailDescriptionText.movementMethod = ScrollingMovementMethod()

        binding.detailDescriptionText.setOnClickListener {
            findNavController().navigate(R.id.toMemoEditFragment)
        }
        return binding.root
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