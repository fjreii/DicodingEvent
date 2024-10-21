package com.mdproject.dicodingevent.ui.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.R
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.FragmentUpcomingBinding
import com.mdproject.dicodingevent.ui.EventsAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val upcomingViewModel by viewModels<UpcomingViewModel>()

    private lateinit var eventsAdapter: EventsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserver()
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter { selectedEvent ->
            val action = UpcomingFragmentDirections.actionNavigationUpcomingToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }
        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }
    }

    private fun setupObserver() {
        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { eventsAdapter.submitList(it) }
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { binding.upcomingLoading.visibility = if (it) View.VISIBLE else View.GONE }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


