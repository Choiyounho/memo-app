package com.soten.memo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soten.memo.data.db.entity.MemoEntity
import com.soten.memo.databinding.ItemMemoBinding

class MemoAdapter(
    private val memoClickedListener: (MemoEntity) -> Unit
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
        Log.d("ASDF", "when")
        notifyDataSetChanged()
    }

    inner class MemoViewHolder(private val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(memoEntity: MemoEntity) {
            binding.memoTitleText.text = memoEntity.title
            binding.memoDescriptionText.text = memoEntity.description

            binding.root.setOnClickListener {
                memoClickedListener(memoEntity)
            }
        }

    }
}