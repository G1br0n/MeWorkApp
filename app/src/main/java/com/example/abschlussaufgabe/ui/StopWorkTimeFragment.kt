package com.example.abschlussaufgabe.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentStopWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import java.time.Duration
import java.time.LocalDateTime

/**
 * Dieses Fragment dient zum Stoppen der Arbeitszeit des Benutzers.
 */
class StopWorkTimeFragment : Fragment() {

    private lateinit var binding: FragmentStopWorkTimeBinding

    private val fireStore: FireStoreViewModel by activityViewModels()
    private val viewModel: MainViewModel by activityViewModels()

    private var workReportString = ""  // String für die komplette Arbeitszeitliste des Benutzers
    private var workTimeResult = ""    // Teilstring für die Arbeitszeitliste

    private var latitude = ""
    private var longitude = ""

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    /**
     * Erstellt die View für das Fragment.
     */
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

    /**
     * Wird aufgerufen, nachdem die View erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GPS-Position des Benutzers abrufen
        viewModel.getGPSLocation(requireActivity()) { locationList ->
            latitude = locationList[0].toString()
            longitude = locationList[1].toString()
        }

        // Benutzerdaten von Firestore beobachten und UI aktualisieren
        fireStore.currentUserStore.observe(viewLifecycleOwner) {
            binding.tvPosition.text = "Sie arbeiten als: ${it.timerMap["position"]}"
            binding.tvfGeoLocation.text = "Ort: ${it.timerMap["latitude"]} / ${it.timerMap["longitude"]}"
            binding.tvSap.text = "Ihre Auftragsnummer lautet: ${it.timerMap["sap"]}"

            // Startet den Timer basierend auf den gespeicherten Startzeitdaten des Benutzers
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
            // GPS-Position erneut abrufen
            viewModel.getGPSLocation(requireActivity()) { locationList ->
                latitude = locationList[0].toString()
                longitude = locationList[1].toString()
            }

            // Zeigt einen Bestätigungsdialog an, wenn der "Stop"-Button geklickt wird
            showConfirmationDialog()
        }
    }

    /**
     * Startet einen Timer, der die verstrichene Arbeitszeit seit dem Startzeitpunkt anzeigt.
     */
    private fun startTimer(year: Int, month: Int, day: Int, hour: Int, min: Int, sek: Int) {

        val startTime = LocalDateTime.of(year, month, day, hour, min, sek)
        var totalHours: Long = 0
        var minutes: Long = 0
        var seconds: Long = 0

        runnable = object : Runnable {

            override fun run() {
                // Dauer seit dem Startzeitpunkt berechnen
                val duration = Duration.between(startTime, LocalDateTime.now())

                totalHours = duration.toHours()
                minutes = duration.toMinutes() % 60
                seconds = duration.seconds % 60

                // Update der UI mit der aktuellen Arbeitszeit
                binding.textClock.text = "${totalHours}h ${minutes}m ${seconds}s"
                workTimeResult = "${totalHours}.${minutes}.${seconds}"

                // Aktualisiert die UI jede Sekunde
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    /**
     * Zeigt einen Dialog zur Bestätigung des Arbeitszeitendes.
     */
    private fun showConfirmationDialog() {

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Bestätigung")
        builder.setMessage("Schicht beenden?")
        builder.setPositiveButton("Ja") { dialog, _ ->
            Toast.makeText(requireContext(), "Schönen Feierabend", Toast.LENGTH_SHORT).show()

            // Daten des Benutzers aktualisieren und speichern
            fireStore.userData = fireStore.currentUserStore.value!!
            fireStore.userData.timerStatus = false
            fireStore.saveUserDataStore(fireStore.userData)
            fireStore.getUserDataStore(fireStore.currentUserStore.value!!.userUid)

            val time = LocalDateTime.now()

            //Coder für UserWorkTimList
            workReportString = "${fireStore.currentUserStore.value!!.timerMap["latitude"]} ${fireStore.currentUserStore.value!!.timerMap["longitude"]} $latitude $longitude ${fireStore.currentUserStore.value!!.timerMap["startWeek"]}.${fireStore.currentUserStore.value!!.timerMap["startYear"]!!}.${fireStore.currentUserStore.value!!.timerMap["startMonth"]!!}.${fireStore.currentUserStore.value!!.timerMap["startDay"]!!}.${fireStore.currentUserStore.value!!.timerMap["startHour"]!!}.${fireStore.currentUserStore.value!!.timerMap["startMin"]!!}.${fireStore.currentUserStore.value!!.timerMap["startSek"]!!} ${time.dayOfWeek.name.split("")[1]}${time.dayOfWeek.name.split("")[2]}.${time.year}.${time.month.value}.${time.dayOfMonth}.${time.hour}.${time.minute}.${time.second} $workTimeResult ${fireStore.currentUserStore.value!!.timerMap["position"]}"

            // Arbeitsbericht in Firestore speichern
            fireStore.getWorkTimeListStore(viewModel.userData.userUid)
            fireStore._currentTimWorkList.value!!.add(workReportString)
            fireStore.saveUserWorkTimeLogStore(fireStore._currentTimWorkList.value!!)

            // Zum InWorkTimeFragment navigieren
            findNavController().navigate(R.id.inWorkTimeFragment)

        }
        builder.setNegativeButton("Nein") { dialog, _ ->
            Toast.makeText(requireContext(), "Nein-Button geklickt", Toast.LENGTH_SHORT).show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
