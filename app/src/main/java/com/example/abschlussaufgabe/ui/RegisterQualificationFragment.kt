package com.example.abschlussaufgabe.ui

import DatePickerFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentRegisterBinding
import com.example.abschlussaufgabe.databinding.FragmentRegisterQualificationBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel


class RegisterQualificationFragment : Fragment() {
    private lateinit var binding: FragmentRegisterQualificationBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_qualification, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userQualification = mutableListOf<String>()

        binding.etHiB.setOnClickListener {
            DatePickerFragment().showDatePickerDialog(it)
        }

        binding.checkBoxHip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxHip.text.toString())
            } else {
                userQualification.remove(binding.checkBoxHip.text.toString())
            }

        }

        binding.checkBoxSipo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxSipo.text.toString())
            } else {
                userQualification.remove(binding.checkBoxSipo.text.toString())
            }
        }
        binding.checkBoxBm.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxBm.text.toString())
            } else {
                userQualification.remove(binding.checkBoxBm.text.toString())
            }
        }

        binding.checkBoxSakra.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxSakra.text.toString())
            } else {
                userQualification.remove(binding.checkBoxSakra.text.toString())
            }
        }

        binding.checkBoxHib.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxHib.text.toString())
            } else {
                userQualification.remove(binding.checkBoxHib.text.toString())
            }
        }

        binding.checkBoxBup.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxBup.text.toString())
            } else {
                userQualification.remove(binding.checkBoxBup.text.toString())
            }
        }

        binding.checkBoxBe.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxBe.text.toString())
            } else {
                userQualification.remove(binding.checkBoxBe.text.toString())
            }
        }

        binding.checkBoxSaS.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxSaS.text.toString())
            } else {
                userQualification.remove(binding.checkBoxSaS.text.toString())
            }
        }

        binding.checkBoxPlPf.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification.add(binding.checkBoxPlPf.text.toString())
            } else {
                userQualification.remove(binding.checkBoxPlPf.text.toString())
            }
        }





      /*  binding.butReg.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val firstName = binding.etVorname.text.toString()
            val lastName = binding.etName.text.toString()
            val baNumber = binding.etBa.text.toString().toInt()

            fireBase.register(
                email,
                password,
                firstName,
                lastName,
                baNumber,
                userQualification,
            )
        }*/
    }
}