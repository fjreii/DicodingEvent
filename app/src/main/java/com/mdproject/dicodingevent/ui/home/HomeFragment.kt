package com.mdproject.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.FragmentHomeBinding
import com.mdproject.dicodingevent.ui.CarouselAdapter
import com.mdproject.dicodingevent.ui.EventsAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var carouselAdapter: CarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupAdapters()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvHomeUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeFinished.layoutManager = LinearLayoutManager(context)
    }

    private fun setupAdapters() {
        eventsAdapter = EventsAdapter { selectedEvent ->
            navigateToDetailEvent(selectedEvent)
        }

        carouselAdapter = CarouselAdapter { selectedEvent ->
            navigateToDetailEvent(selectedEvent)
        }

        binding.rvHomeUpcoming.adapter = carouselAdapter
        binding.rvHomeFinished.adapter = eventsAdapter
    }

    private fun observeViewModel() {
        homeViewModel.listUpcomingEvents.observe(viewLifecycleOwner) { upcomingEvents ->
            updateUpcomingEventData(upcomingEvents)
        }

        homeViewModel.listFinishedEvents.observe(viewLifecycleOwner) { finishedEvents ->
            updateFinishedEventData(finishedEvents)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            toggleLoadingIndicator(isLoading)
        }
    }

    private fun updateUpcomingEventData(upcomingEvents: List<ListEventsItem>) {
        carouselAdapter.submitList(upcomingEvents)
    }

    private fun updateFinishedEventData(finishedEvents: List<ListEventsItem>) {
        eventsAdapter.submitList(finishedEvents)
    }

    private fun toggleLoadingIndicator(isLoading: Boolean) {
        binding.homeLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToDetailEvent(selectedEvent: ListEventsItem) {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailEventFragment(selectedEvent)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}