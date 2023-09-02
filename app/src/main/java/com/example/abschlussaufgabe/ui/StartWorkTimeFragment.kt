package com.example.abschlussaufgabe.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentStartWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


//TODO: Ungünstiger name am besten würde es StartInWorkTimeFragment oder so was enliches
class StartWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStartWorkTimeBinding

    private val viewModel: MainViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_start_work_time, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fireStore.currentUserStore.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.timerStatus) {
                    findNavController().navigate(R.id.stopWorkTimeFragment)
                }
            }
        }


        try {

            if (viewModel.gpsLiveData.value!!.startHour != null) {
                findNavController().navigate(R.id.stopWorkTimeFragment)
            }

        } catch (ex: Exception) {

        }


        //Spinner ----------------------------------------------------------------------------------
        val positionList = fireStore.currentUserStore.value!!.userQualification.keys.toList()
        val spinner: Spinner = binding.spinner

        var options =
            arrayOf("Wäle_deine_Position")//Optionsliste für dropdownmenü(spinner) mit startwert

        //füle die dropdown list mit list aus UserModel.qwalifikation
        for (i in positionList.indices) {
            options = options.plus(positionList[i])
        }


        var selectPosition = ""
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)

        // Use ContextCompat to get the color and set it as the background color.

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectPosition = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }

        }

        //------------------------------------------------------------------------------------------


        //Datum anzeige
        var adjustedDateString = ""
        var weekName = "${LocalDateTime.now().dayOfWeek.name.split("")[1]}${LocalDateTime.now().dayOfWeek.name.split("")[2]}"
        var dayTimerCheck = 0
        var adjustedDate: LocalDate = LocalDate.now().plusDays(dayTimerCheck.toLong())
        var previousHour = binding.myTimePicker.currentHour
        binding.tvDate.text =
            "$weekName ${LocalDate.now().dayOfMonth.toInt() + dayTimerCheck}.${LocalDate.now().month.value}.${LocalDate.now().year}"

        //TimePicke und datum anzeige anpassung
        binding.myTimePicker.setOnTimeChangedListener { _, hourOfDay, _ ->

            adjustedDate = LocalDate.now().plusDays(dayTimerCheck.toLong())

            if (hourOfDay == 0 && previousHour == 23) {
                dayTimerCheck++  // Add a day when switching from 23 to 00 hour
            } else if (hourOfDay == 23 && previousHour == 0) {
                dayTimerCheck--  // Subtract a day when switching from 00 to 23 hour
            }
            previousHour = hourOfDay
            weekName =
                adjustedDate.dayOfWeek.name.split("")[1] + adjustedDate.dayOfWeek.name.split("")[2]

            // Update date display
            adjustedDateString =
                "$weekName ${adjustedDate.dayOfMonth}.${adjustedDate.monthValue}.${adjustedDate.year}"
            binding.tvDate.text = adjustedDateString

            // todo LOG:
            Log.e(TAG, "$adjustedDate")


            // Use the weekName wherever you need it
        }


        //TimePicker 24 Hour Style
        binding.myTimePicker.setIs24HourView(true)


        //GPS
        val REQUEST_LOCATION_PERMISSION_CODE = 0

        var latitude = "0"
        var longitude = "0"
        //TODO: Test time picker


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.checkBoxSaveCurrentPosition.setOnCheckedChangeListener { buttonView, isChecked ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener() { location: Location ->
                    location.let {


                        latitude = it.latitude.toString()
                        longitude = it.longitude.toString()

                        if (isChecked) {
                            binding.tvSaveCurrentPosition.text = "  $latitude $longitude"

                        } else {
                            binding.tvSaveCurrentPosition.text = ""
                            fireStore.userData.timerMap["latitude"] = ""
                            fireStore.userData.timerMap["longitude"] = ""
                        }


                    }
                    //                        todo neue paramenter
                    //binding.tvFive.text = fusedLocationClient.lastLocation.result.elapsedRealtimeAgeMillis.
                }
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION_CODE
                )
            }
        }


        binding.ibStart.setOnClickListener {

            fireStore.userData = fireStore.currentUserStore.value!!
            fireStore.userData.timerStatus = true
            fireStore.userData.timerMap["startYear"] = adjustedDate.year.toString()
            fireStore.userData.timerMap["startMonth"] = adjustedDate.month.value.toString()
            fireStore.userData.timerMap["startWeek"] = weekName
            fireStore.userData.timerMap["startDay"] = (adjustedDate.dayOfMonth).toString()
            fireStore.userData.timerMap["startHour"] = binding.myTimePicker.hour.toString()
            fireStore.userData.timerMap["startMin"] = binding.myTimePicker.minute.toString()
            fireStore.userData.timerMap["startSek"] = LocalTime.now().second.toString()
            fireStore.userData.timerMap["sap"] = binding.editTextNumberSigned.text.toString()
            fireStore.userData.timerMap["latitude"] = "$latitude"
            fireStore.userData.timerMap["longitude"] = "$longitude"
            fireStore.userData.timerMap["position"] = selectPosition

            fireStore.saveUserDataStore(fireStore.userData)
            fireStore.getUserDataStore(fireStore.currentUserStore.value!!.userUid)

            viewModel.playActionSound(context!!)
            findNavController().navigate(R.id.stopWorkTimeFragment)
        }


    }
}
