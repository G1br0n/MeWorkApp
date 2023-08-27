package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserDataModel

import com.example.abschlussaufgabe.databinding.FragmentStopWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import java.time.Duration
import java.time.LocalTime

class StopWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStopWorkTimeBinding

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var userData: UserDataModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val fireStore: FireStoreViewModel by activityViewModels()


    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable


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



        fireStore.currentUserStore.observe(viewLifecycleOwner){
            binding.tvPosition.text = "Sie arbeiten als: ${it.timerMap["position"]}"
            binding.tvfGeoLocation.text = "Ort: ${it.timerMap["latitude"]} / ${it.timerMap["longitude"]}"
            binding.tvSap.text = "Ihre Auftragsnummer lautet: ${it.timerMap["sap"]}"

            startTimer(

               it.timerMap["startHour"]!!.toInt(),
               it.timerMap["startMin"]!!.toInt(),
               it.timerMap["startSek"]!!.toInt(),
               )
        }




        binding.ibStart.setOnClickListener {
            fireStore.userData = fireStore.currentUserStore.value!!
            fireStore.userData.timerStatus = false

            fireStore.saveUserDataStore(fireStore.userData)
            fireStore.getUserDataStore(fireStore.currentUserStore.value!!.userUid)


            findNavController().navigate(R.id.inWorkTimeFragment)
        }
    }

    private fun startTimer(hour: Int, min: Int, sek: Int) {
        val startTime = LocalTime.of(hour, min, sek)
        runnable = object : Runnable {
            override fun run() {
                val duration = Duration.between(startTime, LocalTime.now())
                val hours = duration.toHours()
                val minutes = duration.minusHours(hours).toMinutes()
                val seconds = duration.seconds % 60

                binding.textClock.text = "${hours}h ${minutes}m ${seconds}s"

                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }
}