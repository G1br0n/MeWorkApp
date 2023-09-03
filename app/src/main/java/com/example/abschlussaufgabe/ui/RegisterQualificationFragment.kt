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
* Ein Fragment zur Eingabe und Registrierung von Benutzerqualifikationen.
*/
class RegisterQualificationFragment : Fragment() {

    // Datenbindung für dieses Fragment.
    private lateinit var binding: FragmentRegisterQualificationBinding

    // ViewModel-Instanzen
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    /**
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
     * Initialisiert die View, nachdem sie erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userQualification = mutableMapOf<String, String>()
        var userData = fireStore.userData

        // Listeners für Checkboxen. Abhängig von der Auswahl wird ein Textfeld ein- oder ausgeblendet.
        // Wenn die Checkbox aktiviert ist, wird das entsprechende Textfeld angezeigt und die Qualifikation hinzugefügt.
        // Wenn die Checkbox deaktiviert ist, wird das Textfeld verborgen und die Qualifikation entfernt.
        binding.checkBoxHib.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.checkBoxHib.isChecked) {
                binding.etHiB.visibility = View.VISIBLE
                userQualification[binding.checkBoxHib.text.toString()] =
                    binding.etHiB.text.toString()
            } else {
                binding.etHiB.visibility = View.GONE
                userQualification.remove(binding.checkBoxHib.text.toString())
            }
        }
        binding.checkBoxHip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etHip.visibility = View.VISIBLE
                userQualification[binding.checkBoxHip.text.toString()] =
                    binding.etHip.text.toString()

            } else {
                binding.etHip.visibility = View.GONE
                userQualification.remove(binding.checkBoxHip.text.toString())
            }

        }
        binding.checkBoxSipo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etSipo.visibility = View.VISIBLE
                userQualification[binding.checkBoxSipo.text.toString()] =
                    binding.etSipo.text.toString()
            } else {
                binding.etSipo.visibility = View.GONE
                userQualification.remove(binding.checkBoxSipo.text.toString())
            }
        }
        binding.checkBoxBm.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etBm.visibility = View.VISIBLE
                userQualification[binding.checkBoxBm.text.toString()] = binding.etBm.text.toString()
            } else {
                binding.etBm.visibility = View.GONE
                userQualification.remove(binding.checkBoxBm.text.toString())
            }
        }
        binding.checkBoxSakra.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etSakre.visibility = View.VISIBLE
                userQualification[binding.checkBoxSakra.text.toString()] =
                    binding.etSakre.text.toString()
            } else {
                binding.etSakre.visibility = View.GONE
                userQualification.remove(binding.checkBoxSakra.text.toString())
            }
        }
        binding.checkBoxBup.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etBup.visibility = View.VISIBLE
                userQualification[binding.checkBoxBup.text.toString()] =
                    binding.etBup.text.toString()
            } else {
                binding.etBup.visibility = View.GONE
                userQualification.remove(binding.checkBoxBup.text.toString())
            }
        }
        binding.checkBoxBe.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etBe.visibility = View.VISIBLE
                userQualification[binding.checkBoxBe.text.toString()] = binding.etBe.text.toString()
            } else {
                binding.etBe.visibility = View.GONE
                userQualification.remove(binding.checkBoxBe.text.toString())
            }
        }
        binding.checkBoxSaS.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etSas.visibility = View.VISIBLE
                userQualification[binding.checkBoxSaS.text.toString()] =
                    binding.etSas.text.toString()
            } else {
                binding.etSas.visibility = View.GONE
                userQualification.remove(binding.checkBoxSaS.text.toString())
            }
        }
        binding.checkBoxPlPf.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etPlPf.visibility = View.VISIBLE
                userQualification[binding.checkBoxPlPf.text.toString()] =
                    binding.etPlPf.text.toString()
            } else {
                binding.etPlPf.visibility = View.GONE
                userQualification.remove(binding.checkBoxPlPf.text.toString())
            }
        }

        // Hinzufügen von Listeners für Datums-Eingabefelder, um DatePickerDialog anzuzeigen.
        binding.etHiB.setOnClickListener {
            viewModel.showDatePicker(binding.etHiB, context!!)
        }
        binding.etHip.setOnClickListener {
            viewModel.showDatePicker(binding.etHip, context!!)
        }
        binding.etSipo.setOnClickListener {
            viewModel.showDatePicker(binding.etSipo, context!!)
        }
        binding.etBm.setOnClickListener {
            viewModel.showDatePicker(binding.etBm, context!!)
        }
        binding.etSakre.setOnClickListener {
            viewModel.showDatePicker(binding.etSakre, context!!)
        }
        binding.etBup.setOnClickListener {
            viewModel.showDatePicker(binding.etBup, context!!)
        }
        binding.etBe.setOnClickListener {
            viewModel.showDatePicker(binding.etBe, context!!)
        }
        binding.etSas.setOnClickListener {
            viewModel.showDatePicker(binding.etSas, context!!)
        }
        binding.etPlPf.setOnClickListener {
            viewModel.showDatePicker(binding.etPlPf, context!!)
        }

        // Listener für den Zurück-Button
        binding.butBac.setOnClickListener {
            findNavController().navigateUp()
        }
        // Listener für den Registrierungsabschluss-Button
        binding.butRegEnd.setOnClickListener {

            // Aktualisiere die Qualifikationsdaten basierend auf den Eingabewerten.
            for (key in userQualification.keys) {
                if (key == "Hib") {
                    userQualification[key] = binding.etHiB.text.toString()
                }
                if (key == "Hip") {
                    userQualification[key] = binding.etHip.text.toString()
                }
                if (key == "BE") {
                    userQualification[key] = binding.etBe.text.toString()
                }
                if (key == "Sipo") {
                    userQualification[key] = binding.etSipo.text.toString()
                }
                if (key == "BM") {
                    userQualification[key] = binding.etBm.text.toString()
                }
                if (key == "Sakra") {
                    userQualification[key] = binding.etSakre.text.toString()
                }
                if (key == "BuP") {
                    userQualification[key] = binding.etBup.text.toString()
                }
                if (key == "PlPf") {
                    userQualification[key] = binding.etPlPf.text.toString()
                }
                if (key == "SaS") {
                    userQualification[key] = binding.etSas.text.toString()
                }
            }
            try {
                // Versuche, den Benutzer mit den eingegebenen Daten zu registrieren.
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



}
