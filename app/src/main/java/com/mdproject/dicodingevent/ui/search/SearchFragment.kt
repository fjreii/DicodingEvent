package com.mdproject.dicodingevent.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.R
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.FragmentSearchBinding
import com.mdproject.dicodingevent.ui.SearchAdapter

@SuppressLint("SetTextI18n")
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private val adapter by lazy { SearchAdapter { navigateToDetail(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SearchFragment.adapter
        }
    }

    private fun setupSearchBar() {
        binding.searchBarEvent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText)
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        searchViewModel.searchEvent(query)
    }

    private fun observeViewModel() {
        searchViewModel.apply {
            isLoading.observe(viewLifecycleOwner, ::showLoading)
            listEvents.observe(viewLifecycleOwner, ::handleEventList)
        }
    }

    private fun handleEventList(events: List<ListEventsItem>?) {
        if (events.isNullOrEmpty()) {
            showEmptyResult()
        } else {
            setEventData(events)
            showSearchResult()
        }
    }

    private fun setEventData(events: List<ListEventsItem>) {
        adapter.submitList(events)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.searchProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyResult() {
        with(binding) {
            rvSearchResult.visibility = View.INVISIBLE
            tvResultEmpty.visibility = View.VISIBLE
            tvSearchResultTitle.visibility = View.INVISIBLE
            cvSearchResult.visibility = View.INVISIBLE
        }
    }

    private fun showSearchResult() {
        with(binding) {
            rvSearchResult.visibility = View.VISIBLE
            tvResultEmpty.visibility = View.GONE
            tvSearchResultTitle.visibility = View.VISIBLE
            cvSearchResult.visibility = View.VISIBLE
        }
    }

    private fun navigateToDetail(selectedEvent: ListEventsItem) {
        val action = SearchFragmentDirections.actionNavigationSearchToDetailEventFragment(selectedEvent)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
