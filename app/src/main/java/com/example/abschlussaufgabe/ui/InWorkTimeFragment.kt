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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.GpsModel
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentInWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.roundToInt


//TODO: Ungünstiger name am besten würde es StartInWorkTimeFragment oder so was enliches
class InWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentInWorkTimeBinding

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var userData: UserDataModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_in_work_time, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userData = viewModel.userData

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Spinner ----------------------------------------------------------------------------------
        val positionList = userData.userQualification
        val spinner: Spinner = binding.spinner

        var options =
            arrayOf("Wäle deine Position")//Optionsliste für dropdownmenü(spinner) mit startwert

        //füle die dropdown list mit list aus UserModel.qwalifikation
        for (i in positionList.indices) {
            options = options.plus(positionList[i])
        }

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        //------------------------------------------------------------------------------------------

        //TimePicker 24 Hour Style
        binding.myTimePicker.setIs24HourView(true)

        //TODO: Test time picker
        binding.ibStart.setOnClickListener {
            binding.tvSaveCurrentPosition.text =
                "  ${binding.myTimePicker.hour}:${binding.myTimePicker.minute}"
        }


        //GPS
        var gpsData: GpsModel = GpsModel("", "", "", "", "", "")

        val REQUEST_LOCATION_PERMISSION_CODE = 0

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.checkBoxSaveCurrentPosition.setOnCheckedChangeListener { buttonView, isChecked ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener() { location: Location ->
                    location.let {


                        val latitude = it.latitude.toString()
                        val longitude = it.longitude.toString()

                        if(isChecked){
                            binding.tvSaveCurrentPosition.text = "  $latitude $longitude"
                        } else {
                            binding.tvSaveCurrentPosition.text = ""
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




    }
}
