package com.soten.memo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soten.memo.R
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.databinding.ItemMemoBinding
import java.io.File

class MemoAdapter
    (private val memoClickedListener: (MemoEntity) -> Unit) :
    ListAdapter<MemoEntity, MemoAdapter.MemoViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        return MemoViewHolder(
            ItemMemoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class MemoViewHolder(private val binding: ItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(memoEntity: MemoEntity) {
            binding.memoTitleText.text = memoEntity.title
            binding.memoDescriptionText.text = memoEntity.description

            binding.root.setOnClickListener {
                memoClickedListener(memoEntity)
            }

            if (memoEntity.images.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(File(memoEntity.images.first()))
                    .thumbnail(
                        Glide.with(binding.root.context)
                            .load(R.drawable.loading)
                    )
                    .error(R.drawable.ic_fail)
                    .into(binding.memoThumbnailImage)
            }
        }

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MemoEntity>() {
            override fun areItemsTheSame(oldItem: MemoEntity, newItem: MemoEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MemoEntity, newItem: MemoEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

}