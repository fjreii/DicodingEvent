package com.mdproject.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.databinding.FragmentFinishedBinding
import com.mdproject.dicodingevent.ui.EventsAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel by viewModels<FinishedViewModel>()

    private lateinit var eventsAdapter: EventsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter { selectedEvent ->
            val action = FinishedFragmentDirections.actionNavigationFinishedToDetailEventFragment(selectedEvent)
            findNavController().navigate(action)
        }
        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }
    }

    private fun observeViewModel() {
        finishedViewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventsAdapter.submitList(events)
        }
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.finishedLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}