package com.souvik.todayindia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.souvik.todayindia.R
import com.souvik.todayindia.databinding.NewFeedLayoutBinding
import com.souvik.todayindia.model.NewsData

class FeedAdapter(
    val list: ArrayList<NewsData>,
    val listner: OnClickListener
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: NewFeedLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NewsData) {
            data.urlToImage?.let{
                binding.cvCard.visibility = View.VISIBLE
                Glide.with(binding.ivImage.context)
                    .load(data.urlToImage)
                    .into(binding.ivImage)
            }
            binding.tvHeading.text = data.title
            binding.tvSubHeading.text = data.description
            binding.container.setOnClickListener {
                listner.onClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val binding = DataBindingUtil.inflate<NewFeedLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.new_feed_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onClick(data: NewsData)
    }
}