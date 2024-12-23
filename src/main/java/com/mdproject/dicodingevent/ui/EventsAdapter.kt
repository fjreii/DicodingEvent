package com.mdproject.dicodingevent.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdproject.dicodingevent.R
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.ItemEventBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EventsAdapter(private val onItemClicked: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, EventsAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClicked)
    }
    class MyViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClicked: (ListEventsItem) -> Unit) {
            binding.tvEventName.text = event.name
            binding.tvEventDescription.text = event.summary
            binding.tvEventType.text = event.category

            binding.tvEventStatus.text = getEventStatus(event)

            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.ivEvent)

            binding.root.setOnClickListener { onItemClicked(event) }
        }

        private fun getEventStatus(event: ListEventsItem): String {
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val beginDateTime = LocalDateTime.parse(event.beginTime, formatter)
            val endDateTime = LocalDateTime.parse(event.endTime, formatter)

            return when {
                currentTime.isAfter(endDateTime) -> "Completed"
                currentTime.isBefore(beginDateTime) -> {
                    val timeUntilStart = java.time.Duration.between(currentTime, beginDateTime)
                    if (timeUntilStart.toHours() <= 24) "${timeUntilStart.toHours()} Hour Left"
                    else "${timeUntilStart.toDays()} Day Left"
                }
                currentTime.isAfter(beginDateTime) && currentTime.isBefore(endDateTime) -> "Ongoing"
                else -> "Canceled"
            }
        }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem == newItem
        }
    }
}
