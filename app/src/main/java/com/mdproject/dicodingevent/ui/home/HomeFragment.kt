package com.mdproject.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.data.local.entity.EventEntity
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.FragmentHomeBinding
import com.mdproject.dicodingevent.ui.CarouselAdapter
import com.mdproject.dicodingevent.ui.EventsAdapter
import com.mdproject.dicodingevent.ui.MainViewModel
import com.mdproject.dicodingevent.ui.ViewModelFactory
import com.mdproject.dicodingevent.utils.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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
        homeViewModel.getUpcomingEvent().observe(viewLifecycleOwner) { events ->
            when (events) {
                is Result.Loading -> {
                    binding.homeLoading.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.homeLoading.visibility = View.GONE
                    carouselAdapter.submitList(events.data)
                }

                is Result.Error -> {
                    binding.homeLoading.visibility = View.GONE
                    Toast.makeText(context, "Error: ${events.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        homeViewModel.getFinishedEvent().observe(viewLifecycleOwner) { events ->
            when (events) {
                is Result.Loading -> {
                    binding.homeLoading.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.homeLoading.visibility = View.GONE
                    eventsAdapter.submitList(events.data.take(5).shuffled())
                }

                is Result.Error -> {
                    binding.homeLoading.visibility = View.GONE
                    Toast.makeText(context, "Error: ${events.error}", Toast.LENGTH_SHORT).show()
                }
            }


//        homeViewModel.listUpcomingEvents.observe(viewLifecycleOwner) { upcomingEvents ->
//            updateUpcomingEventData(upcomingEvents)
//        }
//
//        homeViewModel.listFinishedEvents.observe(viewLifecycleOwner) { finishedEvents ->
//            updateFinishedEventData(finishedEvents)
//        }
//
//        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            toggleLoadingIndicator(isLoading)
//        }

        }
    }

//    private fun updateUpcomingEventData(upcomingEvents: List<EventEntity>) {
//        carouselAdapter.submitList(upcomingEvents)
//    }
//
//    private fun updateFinishedEventData(finishedEvents: List<EventEntity>) {
//        eventsAdapter.submitList(finishedEvents)
//    }
//
//    private fun toggleLoadingIndicator(isLoading: Boolean) {
//        binding.homeLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }

    private fun navigateToDetailEvent(selectedEvent: EventEntity) {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailEventFragment(selectedEvent)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}