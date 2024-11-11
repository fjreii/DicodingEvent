package com.mdproject.dicodingevent.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdproject.dicodingevent.data.local.entity.EventEntity
import com.mdproject.dicodingevent.databinding.ItemEventBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EventsAdapter(private val onItemClicked: (EventEntity) -> Unit) : ListAdapter<EventEntity, EventsAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClicked)
    }
    class MyViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity, onItemClicked: (EventEntity) -> Unit) {
            binding.tvEventName.text = event.name
            binding.tvEventType.text = event.category
            binding.tvEventSummary.text = event.summary

//            binding.tvEventStatus.text = getEventStatus(event)

            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.ivEvent)

            binding.root.setOnClickListener { onItemClicked(event) }
        }

//        private fun getEventStatus(event: ListEventsItem): String {
//            val currentTime = LocalDateTime.now()
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            val beginDateTime = LocalDateTime.parse(event.beginTime, formatter)
//            val endDateTime = LocalDateTime.parse(event.endTime, formatter)
//
//            return when {
//                currentTime.isAfter(endDateTime) -> "Selesai"
//                currentTime.isBefore(beginDateTime) -> {
//                    val timeUntilStart = java.time.Duration.between(currentTime, beginDateTime)
//                    if (timeUntilStart.toHours() <= 24) "${timeUntilStart.toHours()} Jam Lagi"
//                    else "${timeUntilStart.toDays()} Hari Lagi"
//                }
//                currentTime.isAfter(beginDateTime) && currentTime.isBefore(endDateTime) -> "Sedang Berlangsung"
//                else -> "Dibatalkan"
//            }
//        }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
