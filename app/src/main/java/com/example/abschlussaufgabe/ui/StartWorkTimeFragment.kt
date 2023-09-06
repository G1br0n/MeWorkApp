package com.example.abschlussaufgabe.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
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
 * ## Information
 * Dieses Fragment dient zum Starten der Arbeitszeit des Benutzers.
 */
class StartWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentStartWorkTimeBinding

    // ViewModel-Referenzen
    private val viewModel: MainViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()


    private var adjustedDateString = ""
    private var weekName = "${LocalDateTime.now().dayOfWeek.name[0]}${LocalDateTime.now().dayOfWeek.name[1]}"

    private var adjustedDate = LocalDate.now()
    private var latitude = "0"
    private var longitude = "0"
    private var selectedPosition = ""

    /**
     * ## Information
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
     * ## Information
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


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedPosition = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Behandlung, wenn nichts ausgewählt wurde
            }
        }

        // Anzeige und Anpassung von Datum und Uhrzeit
         var dayTimerCheck = 0
         var previousHour = binding.myTimePicker.currentHour
        binding.tvDate.text = "$weekName ${LocalDate.now().dayOfMonth}.${LocalDate.now().month.value}.${LocalDate.now().year}"

        // Listener für Zeitanpassung
        binding.myTimePicker.setOnTimeChangedListener { _, hourOfDay, _ ->
            adjustedDate = LocalDate.now().plusDays(dayTimerCheck.toLong())
            if (hourOfDay == 0 && previousHour == 23) dayTimerCheck++
            if (hourOfDay == 23 && previousHour == 0) dayTimerCheck--
            previousHour = hourOfDay
            weekName = "${adjustedDate.dayOfWeek.name[0]}${adjustedDate.dayOfWeek.name[1]}"
            adjustedDateString =
                "$weekName ${adjustedDate.dayOfMonth}.${adjustedDate.monthValue}.${adjustedDate.year}"
            binding.tvDate.text = adjustedDateString
        }

        // 24-Stunden-Format für den TimePicker setzen
        binding.myTimePicker.setIs24HourView(true)

        // GPS-Positionserfassung und Anzeige

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
            showConfirmationDialog()
        }
    }
       private fun showConfirmationDialog() {

            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Bestätigung")
            builder.setMessage("Auftrag aufzeichnung strarten?")
            builder.setPositiveButton("Ja") { dialog, _ ->

                viewModel.playClickSound(context!!)
                Toast.makeText(requireContext(), "Ruhigen schicht!\nAchten Sie auf Ihre sicherheit!", Toast.LENGTH_SHORT).show()

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
                fireStore.userData.timerMap["position"] = selectedPosition

                fireStore.saveUserDataStore(fireStore.userData)
                fireStore.getUserDataStore(fireStore.currentUserStore.value!!.userUid)

                viewModel.playActionSound(context!!)
                findNavController().navigate(R.id.inWorkTimeFragment)
            }
            builder.setNegativeButton("Nein") { dialog, _ ->

                viewModel.playClickSound(context!!)
                Toast.makeText(
                    requireContext(),
                    "Start abgebrochen",
                    Toast.LENGTH_SHORT
                ).show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            //Farben anpassung für alert dialog

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            dialog.window!!.setBackgroundDrawableResource(R.color.back_ground)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)

        }
}
