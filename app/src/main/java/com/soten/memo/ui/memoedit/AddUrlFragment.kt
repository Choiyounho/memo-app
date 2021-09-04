package com.soten.memo.ui.memoedit

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.soten.memo.R
import com.soten.memo.databinding.FragmentAddUrlBinding

class AddUrlFragment : DialogFragment(R.layout.fragment_add_url) {

    private var _binding: FragmentAddUrlBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddUrlBinding.bind(view)

        binding.addUrlButton.setOnClickListener {
            setFragmentResult(
                "requestKey",
                bundleOf("requestKey" to binding.inputUrl.text.toString())
            )
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}