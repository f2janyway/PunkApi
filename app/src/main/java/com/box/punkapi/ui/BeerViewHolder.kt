package com.box.punkapi.ui

import androidx.recyclerview.widget.RecyclerView
import com.box.punkapi.R
import com.box.punkapi.databinding.BeerListItemBinding
import com.box.punkapi.model.Beer
import com.bumptech.glide.Glide

class BeerViewHolder(private val binding: BeerListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Beer) {
        binding.beer = item
        Glide.with(itemView.context)
            .load(item.image_url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.itemImageView)

    }

}