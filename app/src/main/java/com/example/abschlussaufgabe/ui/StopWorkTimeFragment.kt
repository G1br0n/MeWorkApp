package com.example.abschlussaufgabe.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentStopWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import java.time.Duration
import java.time.LocalDateTime


class StopWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStopWorkTimeBinding
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


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Beobachte fireStoreData zu liveDate und passe den layout an
        fireStore.currentUserStore.observe(viewLifecycleOwner) {
            binding.tvPosition.text = "Sie arbeiten als: ${it.timerMap["position"]}"
            binding.tvfGeoLocation.text = "Ort: ${it.timerMap["latitude"]} / ${it.timerMap["longitude"]}"
            binding.tvSap.text = "Ihre Auftragsnummer lautet: ${it.timerMap["sap"]}"


            //S.u  die funktion sorgt für denflexibelen uhrzeiger lauf im ui und fürt rechen operationen durch
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
            findNavController().navigate(R.id.inWorkTimeFragment)
        }
    }


    private fun startTimer(year: Int, month: Int, day: Int, hour: Int, min: Int, sek: Int) {

        val startTime = LocalDateTime.of(year, month, day, hour, min, sek)

        runnable = object : Runnable {

            override fun run() {
               //Bekomer wert in milis zurück
                val duration = Duration.between(startTime, LocalDateTime.now())

                //benutze die duration Funktionen um zeit auszurechene
                val totalHours = duration.toHours()
                val minutes = duration.toMinutes() % 60
                val seconds = duration.seconds % 60

                //Passe hier das UI an
                binding.textClock.text = "${totalHours}h ${minutes}m ${seconds}s"

                //funktion takt
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())  // Ändern Sie `this@YourActivity` zu `requireContext()`

        builder.setTitle("Bestätigung")
        builder.setMessage("Sind Sie sicher?")

        builder.setPositiveButton("Ja") { dialog, which ->
            // Code, der ausgeführt wird, wenn der "Ja"-Button geklickt wird
            Toast.makeText(requireContext(), "Schönen Feierabend", Toast.LENGTH_SHORT).show()

            fireStore.userData = fireStore.currentUserStore.value!!
            fireStore.userData.timerStatus = false
            fireStore.saveUserDataStore(fireStore.userData)
            fireStore.getUserDataStore(fireStore.currentUserStore.value!!.userUid)

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