package com.mdproject.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mdproject.dicodingevent.R
import com.mdproject.dicodingevent.data.local.entity.EventEntity
import com.mdproject.dicodingevent.databinding.FragmentDetailBinding
import com.mdproject.dicodingevent.ui.MainViewModel
import com.mdproject.dicodingevent.ui.ViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailEventViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailEventViewModel.setEvent(args.selectedEvent)

        detailEventViewModel.event.observe(viewLifecycleOwner) { event ->
            binding.apply {
                changeFavoriteIcon(event.isFavorite)
                tvDetailName.text = event.name
                tvDetailOrganizer.text = "Organizer: ${event.ownerName}"
                tvDetailQuota.text = "Remaining Quota: ${event.quota - event.registrants} Slot"
                tvDetailType.text = "Category: ${event.category}"
                tvDetailDescription.text = Html.fromHtml(cleanDescription(event.description), Html.FROM_HTML_MODE_LEGACY)
                tvDetailTime.text = formatEventTime(event)

                fabFavorite.setOnClickListener {
                    event.isFavorite = !event.isFavorite
                    changeFavoriteIcon(event.isFavorite)
                    handleFavoriteChange(event)
                }

                Glide.with(requireContext())
                    .load(event.mediaCover)
                    .into(imgDetailPhoto)

                btnRegister.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.link)))
                }
            }
        }
    }

    private fun cleanDescription(description: String): String {
        return description.replace("\\u003C", "<").replace("\\u003E", ">")
    }

    private fun formatEventTime(event: EventEntity): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("id", "ID"))

        val beginDateTime = LocalDateTime.parse(event.beginTime, inputFormatter)
        val endDateTime = LocalDateTime.parse(event.endTime, inputFormatter)

        return "${beginDateTime.format(outputFormatter)} - ${endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} WIB"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailEventLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleFavoriteChange(event: EventEntity) {
        val message = if (event.isFavorite) {
            detailEventViewModel.addEventToFavorite(event)
            "Berhasil menambahkan event ke Favorit"
        } else {
            detailEventViewModel.removeEventFromFavorite(event)
            "Berhasil menghapus event dari Favorit"
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun changeFavoriteIcon(isFavorite: Boolean) {
        binding.fabFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_black_24 else R.drawable.ic_favorite_border_24
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}