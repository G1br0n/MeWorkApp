package com.example.abschlussaufgabe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentInWorkTimeBinding
import com.example.abschlussaufgabe.databinding.FragmentStopWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient

class StopWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStopWorkTimeBinding

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var userData: UserDataModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stop_work_time, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userData = viewModel.userData

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gpsLiveData.observe(viewLifecycleOwner){
            binding.tvPosition.text = "Sie arbeiten als: ${it.position}"
            binding.tvfGeoLocation.text = "Ort: ${it.latitude} / ${it.longitude}"
            binding.tvSap.text = "Ihre Auftragsnummer lautet: ${it.sap}"
        }


        binding.ibStart.setOnClickListener {
            findNavController().navigate(R.id.inWorkTimeFragment)
        }
    }
}