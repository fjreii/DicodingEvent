package com.mdproject.dicodingevent.ui.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.databinding.FragmentUpcomingBinding
import com.mdproject.dicodingevent.ui.EventsAdapter
import com.mdproject.dicodingevent.ui.MainViewModel
import com.mdproject.dicodingevent.ui.ViewModelFactory
import com.mdproject.dicodingevent.utils.Result

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventsAdapter
    private val upcomingViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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
        upcomingViewModel.getUpcomingEvent().observe(viewLifecycleOwner) { events ->
            when (events) {
                is Result.Loading -> {
                    binding.upcomingLoading.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.upcomingLoading.visibility = View.GONE
                    eventsAdapter.submitList(events.data)
                }

                is Result.Error -> {
                    binding.upcomingLoading.visibility = View.GONE
                    Toast.makeText(context, "Error: ${events.error}", Toast.LENGTH_SHORT).show()
                }
            }
//        upcomingViewModel.listEvents.observe(viewLifecycleOwner) { eventsAdapter.submitList(it) }
//        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { binding.upcomingLoading.visibility = if (it) View.VISIBLE else View.GONE }
//        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) {errorMessage ->
//            errorMessage?.let { showError(it) }}
        }

    }
//    private fun showError(errorMessage: String) {
//        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


