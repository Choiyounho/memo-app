package com.soten.memo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soten.memo.R
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.ItemPhotoBinding
import java.io.File

class PhotoAdapter(
    private val removePhotoListener: (String) -> Unit,
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    var memoState: MemoState? = MemoState.WRITE

    private val items = ArrayList<String>()

    fun setImages(mediaList: List<String>) {
        items.clear()
        items.addAll(mediaList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PhotoViewHolder(ItemPhotoBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.setImage(items[position])
        holder.removeImage(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setImage(path: String) {
            Glide.with(binding.root.context)
                .load(File(path))
                .thumbnail(
                    Glide.with(binding.root.context)
                        .load(R.drawable.loading)
                )
                .error(R.drawable.ic_fail)
                .into(binding.photo)

            if (memoState == MemoState.WRITE) {
                binding.removeButton.visibility = View.VISIBLE
            } else {
                binding.removeButton.visibility = View.GONE
            }
        }

        fun removeImage(path: String) {
            binding.removeButton.setOnClickListener {
                removePhotoListener(path)
            }
        }
    }

}

