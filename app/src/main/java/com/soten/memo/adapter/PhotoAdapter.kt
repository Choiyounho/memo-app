package com.soten.memo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.ItemPhotoBinding
import java.io.File

class PhotoAdapter(
    private val removePhotoListener: (String) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.MainViewHolder>() {

    var memoState: MemoState? = MemoState.WRITE

    private val items = ArrayList<String>()

    fun setImages(mediaList: List<String>) {
        items.clear()
        items.addAll(mediaList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MainViewHolder(ItemPhotoBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.setImage(items[position])
        holder.removeImage(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class MainViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setImage(path: String) {
//            Glide.with(binding.photo).load(Uri.fromFile(File(path)).into(binding.photo)
            binding.photo.setImageURI(Uri.parse(path))

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

