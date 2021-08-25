package com.soten.memo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soten.memo.data.db.entity.MemoState
import com.soten.memo.databinding.ItemPhotoBinding

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.MainViewHolder>() {

    var memoState: MemoState? = MemoState.WRITE

    private val items = ArrayList<Uri>()

    fun setImages(mediaList: List<Uri>) {
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
    }

    override fun getItemCount(): Int = items.size

    inner class MainViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setImage(media: Uri) {
            Glide.with(binding.photo).load(media.toString()).into(binding.photo)

            if (memoState == MemoState.WRITE) {
                binding.removeButton.visibility = View.VISIBLE
            }
        }
    }

}

