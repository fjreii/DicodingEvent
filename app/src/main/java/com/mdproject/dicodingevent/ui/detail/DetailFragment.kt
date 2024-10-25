package com.mdproject.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.databinding.FragmentDetailBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("DEPRECATION")
class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailEventViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailEventViewModel.setEvent(args.selectedEvent)

        detailEventViewModel.event.observe(viewLifecycleOwner) { event ->
            binding.apply {
                tvDetailName.text = event.name
                tvDetailOrganizer.text = "Organizer: ${event.ownerName}"
                tvDetailQuota.text = "Remaining Quota: ${event.quota - event.registrants} Slot"
                tvDetailType.text = "Category: ${event.category}"
                tvDetailDescription.text = Html.fromHtml(cleanDescription(event.description), Html.FROM_HTML_MODE_COMPACT)
                tvDetailTime.text = formatEventTime(event)

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

    private fun formatEventTime(event: ListEventsItem): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("id", "ID"))

        val beginDateTime = LocalDateTime.parse(event.beginTime, inputFormatter)
        val endDateTime = LocalDateTime.parse(event.endTime, inputFormatter)

        return "${beginDateTime.format(outputFormatter)} - ${endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} WIB"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailEventLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
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