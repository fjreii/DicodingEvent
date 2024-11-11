package com.mdproject.dicodingevent.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdproject.dicodingevent.R
import com.mdproject.dicodingevent.databinding.FragmentFavoriteBinding
import com.mdproject.dicodingevent.ui.EventsAdapter
import com.mdproject.dicodingevent.ui.MainViewModel
import com.mdproject.dicodingevent.ui.ViewModelFactory

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventsAdapter
    private val favoriteViewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentFavoriteBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavoriteEvent.apply {
            adapter = this@FavoriteFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        observeFavorites()
    }

    private fun observeFavorites() {
        favoriteViewModel.getFavoriteEvent().observe(viewLifecycleOwner) { favEvent ->
            binding.apply {
                favoriteLoading.visibility = View.GONE
//                favoriteCount.text = "Total: ${favEvent.size} Event"
                rvFavoriteEvent.visibility = if (favEvent.isEmpty()) View.GONE else View.VISIBLE
//                favoriteCount.visibility = favorite.visibility
                adapter.submitList(favEvent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}