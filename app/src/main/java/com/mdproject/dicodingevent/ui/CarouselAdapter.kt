package com.mdproject.dicodingevent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.CarouselEventBinding

class CarouselAdapter (
    private val onItemClicked: (ListEventsItem) -> Unit
) : ListAdapter<ListEventsItem, CarouselAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CarouselEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)

        holder.binding.tvCarouselItemName.text = event.name
        Glide.with(holder.binding.root.context)
            .load(event.imageLogo)
            .into(holder.binding.imgCarouselPhoto)

        holder.binding.root.setOnClickListener {
            onItemClicked(event)
        }
    }

    class MyViewHolder(val binding: CarouselEventBinding) :
        RecyclerView.ViewHolder(binding.root)


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {

                return oldItem == newItem
            }
        }

    }
}
