package com.box.punkapi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.box.punkapi.R
import com.box.punkapi.databinding.BeerListItemBinding
import com.box.punkapi.model.Beer

class BeerListAdapter(private val click: (beer: Beer, extra: FragmentNavigator.Extras) -> Unit) :
    ListAdapter<Beer, BeerViewHolder>(BeerDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BeerListItemBinding.inflate(inflater, parent, false)
        return BeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            it.findViewById<ImageView>(R.id.item_imageView).apply {
                transitionName = item.id.toString()
                val extra = FragmentNavigatorExtras(
                    this to transitionName
                )
                click(item, extra)
            }
        }
    }


    companion object {
        private val BeerDiffUtil = object : DiffUtil.ItemCallback<Beer>() {
            override fun areItemsTheSame(oldItem: Beer, newItem: Beer): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Beer, newItem: Beer): Boolean {
                return oldItem == newItem
            }

        }
    }


}