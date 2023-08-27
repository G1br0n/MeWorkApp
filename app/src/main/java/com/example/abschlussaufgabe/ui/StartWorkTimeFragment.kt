package com.example.abschlussaufgabe.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
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
import com.example.abschlussaufgabe.data.model.WorkRunModel
import com.example.abschlussaufgabe.databinding.FragmentStartWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.LocalTime


//TODO: Ungünstiger name am besten würde es StartInWorkTimeFragment oder so was enliches
class StartWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStartWorkTimeBinding

    private val viewModel: MainViewModel by activityViewModels()


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val fireStore: FireStoreViewModel by activityViewModels()

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


        var gpsData: WorkRunModel = WorkRunModel("", "", "", "", 0, 0, 0, 0, 0, 0)


        //Spinner ----------------------------------------------------------------------------------
        val positionList = fireStore.currentUserStore.value!!.userQualification.keys.toList()
        val spinner: Spinner = binding.spinner

        var options =
            arrayOf("Wäle deine Position")//Optionsliste für dropdownmenü(spinner) mit startwert

        //füle die dropdown list mit list aus UserModel.qwalifikation
        for (i in positionList.indices) {
            options = options.plus(positionList[i])
        }


        var selectPosition = ""
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter
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


        //datum anzeige
        var dayTimerCheck = 0
        var previousHour = binding.myTimePicker.currentHour
        binding.tvDate.text = "${LocalDate.now().dayOfMonth.toInt() + dayTimerCheck}.${LocalDate.now().month.value}.${LocalDate.now().year}"

        //TimePicke und datum anzeige anpassung
        binding.myTimePicker.setOnTimeChangedListener { _, hourOfDay, _ ->
            if (hourOfDay == 0 && previousHour == 23) {
                dayTimerCheck++  // Ein Tag hinzufügen, wenn von 23 auf 00 Uhr gewechselt wird
                binding.tvDate.text = "${LocalDate.now().dayOfMonth.toInt() + dayTimerCheck}.${LocalDate.now().month.value}.${LocalDate.now().year}"
            } else if (hourOfDay == 23 && previousHour == 0) {
                dayTimerCheck--  // Ein Tag subtrahieren, wenn von 00 auf 23 Uhr gewechselt wird
                binding.tvDate.text = "${LocalDate.now().dayOfMonth.toInt() + dayTimerCheck}.${LocalDate.now().month.value}.${LocalDate.now().year}"
            }
            previousHour = hourOfDay
        }


        //TimePicker 24 Hour Style
        binding.myTimePicker.setIs24HourView(true)


        //GPS
        val REQUEST_LOCATION_PERMISSION_CODE = 0

        var latitude = ""
        var longitude = ""
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
            fireStore.userData.timerMap["startYear"] = LocalDate.now().year.toString()
            fireStore.userData.timerMap["startMonth"] = LocalDate.now().month.value.toString()
            fireStore.userData.timerMap["startDay"] = (LocalDate.now().dayOfMonth + dayTimerCheck).toString()
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
