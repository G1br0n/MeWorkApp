package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentStartWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Dieses Fragment dient zum Starten der Arbeitszeit des Benutzers.
 */
class StartWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStartWorkTimeBinding

    // ViewModel-Referenzen
    private val viewModel: MainViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    /**
     * Erstellt die View für das Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_start_work_time, container, false)
        return binding.root
    }

    /**
     * Wird aufgerufen, nachdem die View erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Überwacht den Status des aktuellen Benutzers
        fireStore.currentUserStore.observe(viewLifecycleOwner) {
            if (it?.timerStatus == true) {
                findNavController().navigate(R.id.stopWorkTimeFragment)
            }
        }

        // Überprüft, ob ein Startzeitwert gesetzt ist
        try {
            if (viewModel.gpsLiveData.value?.startHour != null) {
                findNavController().navigate(R.id.stopWorkTimeFragment)
            }
        } catch (ex: Exception) {
            // Fehlerbehandlung kann hier hinzugefügt werden
        }

        // Initialisierung des Dropdowns (Spinners) für die Positionsauswahl
        val positionList = fireStore.currentUserStore.value?.userQualification?.keys?.toList()
        val spinner: Spinner = binding.spinner
        var options = arrayOf("Wäle_deine_Position")
        positionList?.forEach { options += it }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)

        var selectPosition = ""
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectPosition = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Behandlung, wenn nichts ausgewählt wurde
            }
        }

        // Anzeige und Anpassung von Datum und Uhrzeit
        var adjustedDateString = ""
        var weekName = "${LocalDateTime.now().dayOfWeek.name[0]}${LocalDateTime.now().dayOfWeek.name[1]}"
        var dayTimerCheck = 0
        var adjustedDate = LocalDate.now()
        var previousHour = binding.myTimePicker.currentHour
        binding.tvDate.text = "$weekName ${LocalDate.now().dayOfMonth}.${LocalDate.now().month.value}.${LocalDate.now().year}"

        // Listener für Zeitanpassung
        binding.myTimePicker.setOnTimeChangedListener { _, hourOfDay, _ ->
            adjustedDate = LocalDate.now().plusDays(dayTimerCheck.toLong())
            if (hourOfDay == 0 && previousHour == 23) dayTimerCheck++
            if (hourOfDay == 23 && previousHour == 0) dayTimerCheck--
            previousHour = hourOfDay
            weekName = "${adjustedDate.dayOfWeek.name[0]}${adjustedDate.dayOfWeek.name[1]}"
            adjustedDateString = "$weekName ${adjustedDate.dayOfMonth}.${adjustedDate.monthValue}.${adjustedDate.year}"
            binding.tvDate.text = adjustedDateString
        }

        // 24-Stunden-Format für den TimePicker setzen
        binding.myTimePicker.setIs24HourView(true)

        // GPS-Positionserfassung und Anzeige
        var latitude = "0"
        var longitude = "0"
        binding.checkBoxSaveCurrentPosition.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.getGPSLocation(requireActivity()) { locationList ->
                    latitude = locationList[0].toString()
                    longitude = locationList[1].toString()
                    binding.tvSaveCurrentPosition.text = "  $latitude $longitude"
                }
            } else {
                binding.tvSaveCurrentPosition.text = ""
                fireStore.userData.timerMap["latitude"] = ""
                fireStore.userData.timerMap["longitude"] = ""
            }
        }

        // Logik für den "Start"-Button
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
