package com.bangkit.submissionintermediateandroid.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.submissionintermediateandroid.data.DataUser
import com.bangkit.submissionintermediateandroid.data.response.ListStoryItem
import com.bangkit.submissionintermediateandroid.databinding.ItemRowStoryBinding
import com.bangkit.submissionintermediateandroid.ui.activity.DetailStoryActivity
import com.bumptech.glide.Glide

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder> (DIFF_CALLBACK) {
    inner class ViewHolder(private var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            Glide.with(itemView.context)
                .load(storyItem.photoUrl)
                .into(binding.imgItemPhoto)
            binding.tvUsername.text = storyItem.name
            binding.tvDescription.text = storyItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

//        holder.bind(story)

        holder.itemView.setOnClickListener{
            val dataUser = DataUser(
                story!!.id,
                story.photoUrl,
                story.name,
                story.description
            )

            val intentDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intentDetail.putExtra(DetailStoryActivity.TAG, dataUser)
            holder.itemView.context.startActivity(intentDetail, ActivityOptionsCompat.makeSceneTransitionAnimation(holder.itemView.context as Activity).toBundle())
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}