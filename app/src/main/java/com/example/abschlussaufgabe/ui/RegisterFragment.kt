package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.example.abschlussaufgabe.databinding.FragmentRegisterBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * Fragment zur Benutzerregistrierung.
 */
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    /**
     * Wird aufgerufen, nachdem die View erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setzt das Profilbild des Benutzers.
        binding.imageView3.setImageResource(R.drawable.avatar_logo)

        // Button, um zum vorherigen Bildschirm zurückzukehren.
        binding.butBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Button, um zum nächsten Bildschirm (Registrierungsqualifikation) zu navigieren.
        binding.butNext.setOnClickListener {
            try {
                viewModel.isValidEmail(binding.etEmail.text.toString().replace(" ",""))
                viewModel.isValidPassword(binding.etPassword.text.toString().replace(" ",""))

                fireStore.userData = updateUser(fireStore.userData)
                findNavController().navigate(R.id.registerQualificationFragment)
            } catch (ex: Exception) {
                Toast.makeText(requireContext(), ex.message , Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Aktualisiert die Benutzerdaten basierend auf den Eingabefeldern.
     *
     * @param user Das aktuelle UserTestDataModel-Objekt.
     * @return Das aktualisierte UserTestDataModel-Objekt.
     */
    fun updateUser(user: UserTestDataModel): UserTestDataModel {
        user.email = binding.etEmail.text.toString().replace(" ","")
        user.password = binding.etPassword.text.toString().replace(" ","")
        user.firstName = binding.etVorname.text.toString().replace(" ","")
        user.lastName = binding.etName.text.toString().replace(" ","")
        user.baNumber = binding.etBa.text.toString().replace(" ","").toInt()
        user.userQualification = mapOf()
        return user
    }
}
