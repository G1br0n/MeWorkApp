package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * ## Information
 * Haupt-Fragment für die Arbeitserfassung des Benutzers.
 */
class WorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentWorkTimeBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * ## Information
     * Erstellt die View für das Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // FragmentContainerView 2 von 2, zuständig für die Nebenfragmente für Start/Stop.
        // Arbeitet mit nav_time.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_time, container, false)
        return binding.root
    }

    /**
     * ## Information
     * Wird aufgerufen, nachdem die View erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Klick auf den "MeWorkTime"-Button
        // Spielt einen Klick-Sound ab und navigiert zum UserWorkTimeListFragment.
        binding.ibMeWorkTime.setOnClickListener {
            viewModel.playClickSound(context!!)
            findNavController().navigate(R.id.userWorkTimeListFragment)
        }

        // Die folgenden Buttons sind derzeit inaktiv.
        // Bei einem Klick darauf wird ein "Locked"-Sound abgespielt und eine Toast-Nachricht angezeigt.

        binding.ibMeWorkPlan.setOnClickListener {
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }

        binding.ibReportSick.setOnClickListener {
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }

        binding.imageButton10.setOnClickListener {
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }

        binding.imageButton11.setOnClickListener {
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }

        binding.imageButton12.setOnClickListener {
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }
    }
}
