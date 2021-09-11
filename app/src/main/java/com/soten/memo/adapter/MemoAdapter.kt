package com.soten.memo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soten.memo.R
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.databinding.ItemMemoBinding
import java.io.File

class MemoAdapter(
    private val memoClickedListener: (MemoEntity) -> Unit,
) : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    var memoList: List<MemoEntity> = emptyList()

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
        holder.bind(memoList[position])
    }

    override fun getItemCount(): Int = memoList.size

    fun setMemo(memoList: List<MemoEntity>) {
        this.memoList = memoList
        notifyDataSetChanged()
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
}