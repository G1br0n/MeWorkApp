package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentRegisterQualificationBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * ## Information
 * Ein Fragment zur Eingabe und Registrierung von Benutzerqualifikationen.
 */
class RegisterQualificationFragment : Fragment() {

    // Datenbindung für dieses Fragment.
    private lateinit var binding: FragmentRegisterQualificationBinding

    // ViewModel-Instanzen
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    private var userQualification = mutableMapOf<String, String>()
    var userData = fireStore.userData

    /**
     * ## Information
     * Erstellt die View für das Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register_qualification,
            container,
            false
        )
        return binding.root
    }

    /**
     * ## Information
     * Initialisiert die View, nachdem sie erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aufruf der Methode für jede CheckBox:
        viewModel.setupCheckbox(binding.checkBoxHib, binding.etHiB, userQualification)
        viewModel.setupCheckbox(binding.checkBoxHip, binding.etHip, userQualification)
        viewModel.setupCheckbox(binding.checkBoxSipo, binding.etSipo, userQualification)
        viewModel.setupCheckbox(binding.checkBoxBm, binding.etBm, userQualification)
        viewModel.setupCheckbox(binding.checkBoxSakra, binding.etSakre, userQualification)
        viewModel.setupCheckbox(binding.checkBoxBup, binding.etBup, userQualification)
        viewModel.setupCheckbox(binding.checkBoxBe, binding.etBe, userQualification)
        viewModel.setupCheckbox(binding.checkBoxSaS, binding.etSas, userQualification)
        viewModel.setupCheckbox(binding.checkBoxPlPf, binding.etPlPf, userQualification)

        // Hinzufügen von Listeners für Datums-Eingabefelder, um DatePickerDialog anzuzeigen.
        binding.etHiB.setOnClickListener { viewModel.showDatePicker(binding.etHiB, context!!) }
        binding.etHip.setOnClickListener { viewModel.showDatePicker(binding.etHip, context!!) }
        binding.etSipo.setOnClickListener { viewModel.showDatePicker(binding.etSipo, context!!) }
        binding.etBm.setOnClickListener { viewModel.showDatePicker(binding.etBm, context!!) }
        binding.etSakre.setOnClickListener { viewModel.showDatePicker(binding.etSakre, context!!) }
        binding.etBup.setOnClickListener { viewModel.showDatePicker(binding.etBup, context!!) }
        binding.etBe.setOnClickListener { viewModel.showDatePicker(binding.etBe, context!!) }
        binding.etSas.setOnClickListener { viewModel.showDatePicker(binding.etSas, context!!) }
        binding.etPlPf.setOnClickListener { viewModel.showDatePicker(binding.etPlPf, context!!) }

        // Listener für den Zurück-Button
        binding.butBac.setOnClickListener {
            findNavController().navigateUp()
        }

        // Listener für den Registrierungsabschluss-Button
        binding.butRegEnd.setOnClickListener {
            // Aktualisiere die Qualifikationsdaten basierend auf den Eingabewerten.

            // Erstelle eine Zuordnung von Schlüsseln zu den zugehörigen EditText-Bindings.
            val keyToEditTextBinding = mapOf(
                "Hib" to binding.etHiB,
                "Hip" to binding.etHip,
                "BE" to binding.etBe,
                "Sipo" to binding.etSipo,
                "BM" to binding.etBm,
                "Sakra" to binding.etSakre,
                "BuP" to binding.etBup,
                "PlPf" to binding.etPlPf,
                "SaS" to binding.etSas
            )

            // Iteriere über alle Schlüssel in userQualification.
            for (key in userQualification.keys) {
                keyToEditTextBinding[key]?.let {
                    userQualification[key] = it.text.toString()
                }
            }
        }
        // Versuche, den Benutzer mit den eingegebenen Daten zu registrieren.
        try {
            fireBase.register(
                context!!,
                userData.email,
                userData.password,
                userData.firstName,
                userData.lastName,
                userData.baNumber,
                userQualification,
            )

            // Navigiere zum Login-Fragment nach erfolgreicher Registrierung.
            findNavController().navigate(R.id.logInFragment)
        } catch (ex: Exception) {
            // Hier könnte man einen Fehler loggen oder dem Benutzer eine Nachricht anzeigen.
        }
    }
}

