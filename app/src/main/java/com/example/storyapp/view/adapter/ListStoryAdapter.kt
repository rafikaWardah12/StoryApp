package com.example.storyapp.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemRowBinding
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.view.detail.DetailStoryActivity

class ListStoryAdapter :
    PagingDataAdapter<ListStoryItem,ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListStoryItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(imgStory)
                txtTitle.text = item.name
                txtDescription.text = item.description
                itemView.setOnClickListener{
                    val intentDetail = Intent(itemView.context, DetailStoryActivity::class.java )
                    intentDetail.putExtra(DetailStoryActivity.EXTRA_ID, item.id)
                    Log.e("klik intent detail", item.id.toString())
                    itemView.context.startActivities(arrayOf(intentDetail))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story!!)
    }

    companion object {
       val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
           override fun areItemsTheSame(
               oldItem: ListStoryItem,
               newItem: ListStoryItem
           ): Boolean {
               return oldItem == newItem
           }

           override fun areContentsTheSame(
               oldItem: ListStoryItem,
               newItem: ListStoryItem
           ): Boolean {
               return oldItem == newItem
           }
       }
    }
}