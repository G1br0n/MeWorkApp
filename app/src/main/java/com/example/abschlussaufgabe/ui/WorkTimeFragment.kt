package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

class WorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentWorkTimeBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //FragmentContainerView 2 von 2 , zuständig für die neben fragmete für start stop fragment, arbeitet mit nav_time
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_time, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//Inacktive buttons spilen loked sound ab und zeigen toasst nachricht an
        binding.ibMeWorkTime.setOnClickListener{
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }
        binding.ibMeWorkPlan.setOnClickListener{
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }
        binding.ibReportSick.setOnClickListener{
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }
        binding.imageButton10.setOnClickListener{
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }
        binding.imageButton11.setOnClickListener{
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }
        binding.imageButton12.setOnClickListener{
            Toast.makeText(requireContext(), "BUTTON INACTIVE", Toast.LENGTH_SHORT).show()
            viewModel.playLockedSound(context!!)
        }
    }
}