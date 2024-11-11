package com.mdproject.dicodingevent.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.R
import com.mdproject.dicodingevent.data.local.entity.EventEntity
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.FragmentSearchBinding
import com.mdproject.dicodingevent.ui.MainViewModel
import com.mdproject.dicodingevent.ui.SearchAdapter
import com.mdproject.dicodingevent.ui.ViewModelFactory
import com.mdproject.dicodingevent.utils.Result

@SuppressLint("SetTextI18n")
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(requireActivity()) }
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
//        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SearchFragment.adapter
        }
    }

    private fun setupSearchBar() {
        binding.searchBarEvent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        searchViewModel.searchEvent(query).observe(viewLifecycleOwner, ::handleSearchResult)
    }

    private fun handleSearchResult(result: Result<List<EventEntity>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                showLoading(false)
                handleEventList(result.data)
            }
            is Result.Error -> {
                showLoading(false)
                showError(result.error)
            }
        }
    }

    private fun handleEventList(events: List<EventEntity>) {
        if (events.isNullOrEmpty()) {
            showEmptyResult()
        } else {
            setEventData(events)
            showSearchResult()
        }
    }

    private fun setEventData(events: List<EventEntity>) {
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

    private fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDetail(selectedEvent: EventEntity) {
        val action = SearchFragmentDirections.actionNavigationSearchToDetailEventFragment(selectedEvent)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
