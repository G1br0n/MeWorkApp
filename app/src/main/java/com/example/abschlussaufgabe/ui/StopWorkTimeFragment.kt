package com.example.abschlussaufgabe.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentStopWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.Duration
import java.time.LocalDateTime


class StopWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStopWorkTimeBinding

    private val fireStore: FireStoreViewModel by activityViewModels()
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var workReportString = ""
    private var workTimeResult = ""
    private var latitude = ""
    private var longitude = ""

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fireStore.getWorkTimeListStore(viewModel.userData.userUid)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stop_work_time, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val REQUEST_LOCATION_PERMISSION_CODE = 0


        //TODO: Test time picker
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener() { location: Location ->
                location.let {
                    latitude = it.latitude.toString()
                    longitude = it.longitude.toString()
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

        //Beobachte fireStoreData zu liveDate und passe den layout an
        fireStore.currentUserStore.observe(viewLifecycleOwner) {
            binding.tvPosition.text = "Sie arbeiten als: ${it.timerMap["position"]}"
            binding.tvfGeoLocation.text =
                "Ort: ${it.timerMap["latitude"]} / ${it.timerMap["longitude"]}"
            binding.tvSap.text = "Ihre Auftragsnummer lautet: ${it.timerMap["sap"]}"


            //S.u  die funktion sorgt für denflexibelen uhrzeiger lauf im ui und fürt rechen operationen durch

            Log.e("Log9", "${it.timerMap["startMonth"]!!.toInt()}")
            startTimer(
                it.timerMap["startYear"]!!.toInt(),
                it.timerMap["startMonth"]!!.toInt(),
                it.timerMap["startDay"]!!.toInt(),
                it.timerMap["startHour"]!!.toInt(),
                it.timerMap["startMin"]!!.toInt(),
                it.timerMap["startSek"]!!.toInt()
            )

        }




        binding.ibStart.setOnClickListener {
            showConfirmationDialog()
        }
    }


    private fun startTimer(year: Int, month: Int, day: Int, hour: Int, min: Int, sek: Int) {

        val startTime = LocalDateTime.of(year, month, day, hour, min, sek)

        var totalHours: Long = 0
        var minutes: Long = 0
        var seconds: Long = 0

        runnable = object : Runnable {

            override fun run() {
                //Bekomer wert in milis zurück
                val duration = Duration.between(startTime, LocalDateTime.now())

                //benutze die duration Funktionen um zeit auszurechene
                totalHours = duration.toHours()
                minutes = duration.toMinutes() % 60
                seconds = duration.seconds % 60

                //Passe hier das UI an
                binding.textClock.text = "${totalHours}h ${minutes}m ${seconds}s"

                //Benutze für Log
                workTimeResult = "${totalHours}.${minutes}.${seconds}"

                //funktion takt
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)

    }

    private fun showConfirmationDialog() {
        val builder =
            AlertDialog.Builder(requireContext())  // Ändern Sie `this@YourActivity` zu `requireContext()`

        builder.setTitle("Bestätigung")
        builder.setMessage("Sind Sie sicher?")

        builder.setPositiveButton("Ja") { dialog, which ->
            // Code, der ausgeführt wird, wenn der "Ja"-Button geklickt wird
            Toast.makeText(requireContext(), "Schönen Feierabend", Toast.LENGTH_SHORT).show()

            fireStore.userData = fireStore.currentUserStore.value!!
            fireStore.userData.timerStatus = false
            fireStore.saveUserDataStore(fireStore.userData)
            fireStore.getUserDataStore(fireStore.currentUserStore.value!!.userUid)

            var time = LocalDateTime.now()


            //Lange unleserliche zahl was eigendlich nur einen string zurückgibt in bestimtem format mit lehr zeichen und punkte. wird im adapter dekodiert
            //startGpsLocation stopGpsLocation startWochenTag.jahr.monatValue.tag.stunde.min.sek stopWochenTag.jahr.monatValue.tag.stunde.min.sek Position
            //0 0 37.4226711 -122.0849872 FR.2023.9.1.23.27.50 FR.2023.9.1.23.27.52 0.0.1 Wäle_deine_Position
            workReportString = "${fireStore.currentUserStore.value!!.timerMap["latitude"]} ${fireStore.currentUserStore.value!!.timerMap["longitude"]} ${latitude} ${longitude} ${fireStore.currentUserStore.value!!.timerMap["startWeek"]}.${fireStore.currentUserStore.value!!.timerMap["startYear"]!!}.${fireStore.currentUserStore.value!!.timerMap["startMonth"]!!}.${fireStore.currentUserStore.value!!.timerMap["startDay"]!!}.${fireStore.currentUserStore.value!!.timerMap["startHour"]!!}.${fireStore.currentUserStore.value!!.timerMap["startMin"]!!}.${fireStore.currentUserStore.value!!.timerMap["startSek"]!!} ${time.dayOfWeek.name.split("")[1]}${time.dayOfWeek.name.split("")[2]}.${time.year}.${time.month.value}.${time.dayOfMonth}.${time.hour}.${time.minute}.${time.second} ${workTimeResult} ${fireStore.currentUserStore.value!!.timerMap["position"]}"

            fireStore.getWorkTimeListStore(viewModel.userData.userUid)
            fireStore._currentTimWorkList.value!!.add(workReportString)
            fireStore.saveUserWorkTimeLogStore(fireStore._currentTimWorkList.value!!)


            findNavController().navigate(R.id.inWorkTimeFragment)


        }

        builder.setNegativeButton("Nein") { dialog, which ->
            // Code, der ausgeführt wird, wenn der "Nein"-Button geklickt wird
            Toast.makeText(requireContext(), "Nein-Button geklickt", Toast.LENGTH_SHORT).show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

}