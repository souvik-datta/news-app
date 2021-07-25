package com.souvik.todayindia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.souvik.todayindia.R
import com.souvik.todayindia.databinding.CategoryLayoutBinding
import com.souvik.todayindia.model.Category


class CategoryAdapter(
    private val list: ArrayList<Category>,
    val onClickListener: OnClickListener) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: CategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Category){
            Glide.with(binding.ivImage.context)
                .load(data.url)
                .into(binding.ivImage)
            binding.tvCatName.text = data.name
            binding.clContainer.setOnClickListener{
                onClickListener.onClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CategoryLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.category_layout,parent,false)
       return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener{
        fun onClick(data:Category)
    }
}
