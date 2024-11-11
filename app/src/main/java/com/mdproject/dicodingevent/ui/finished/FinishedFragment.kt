package com.mdproject.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.databinding.FragmentFinishedBinding
import com.mdproject.dicodingevent.ui.EventsAdapter
import com.mdproject.dicodingevent.ui.MainViewModel
import com.mdproject.dicodingevent.ui.ViewModelFactory
import com.mdproject.dicodingevent.utils.Result

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventsAdapter
    private val finishedViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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
        initRecyclerView()
        observeViewModel()
    }

    private fun initRecyclerView() {
        eventAdapter = EventsAdapter { selectedEvent ->
            val action = FinishedFragmentDirections.actionNavigationFinishedToDetailEventFragment(
                selectedEvent
            )
            findNavController().navigate(action)
        }
        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }

    private fun observeViewModel() {
        finishedViewModel.getFinishedEvent().observe(viewLifecycleOwner) { events ->
            when (events) {
                is Result.Loading -> {
                    binding.finishedLoading.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.finishedLoading.visibility = View.GONE
                    eventAdapter.submitList(events.data)
                }

                is Result.Error -> {
                    binding.finishedLoading.visibility = View.GONE
                    Toast.makeText(context, "Error: ${events.error}", Toast.LENGTH_SHORT).show()
                }
            }

//        finishedViewModel.listEvents.observe(viewLifecycleOwner) { events ->
//            eventAdapter.submitList(events)
//        }
//        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            binding.finishedLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}