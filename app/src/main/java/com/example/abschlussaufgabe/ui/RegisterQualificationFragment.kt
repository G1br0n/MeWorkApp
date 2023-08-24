package com.example.abschlussaufgabe.ui

import DatePickerFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        var userQualification = mutableMapOf<String,String>()
        var userData = fireStore.userData





        binding.checkBoxHib.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.checkBoxHib.isChecked) {
                userQualification[binding.checkBoxHib.text.toString()] = binding.etHiB.text.toString()
            } else {
                userQualification.remove(binding.checkBoxHib.text.toString())
            }
        }

        binding.checkBoxHip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                userQualification[binding.checkBoxHip.text.toString()] = binding.etHip.text.toString()

            } else {
                userQualification.remove(binding.checkBoxHip.text.toString())
            }

        }

        binding.checkBoxSipo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification[binding.checkBoxSipo.text.toString()] = binding.etSipo.text.toString()
            } else {
                userQualification.remove(binding.checkBoxSipo.text.toString())
            }
        }
        binding.checkBoxBm.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification[binding.checkBoxBm.text.toString()] = binding.etBm.text.toString()
            } else {
                userQualification.remove(binding.checkBoxBm.text.toString())
            }
        }

        binding.checkBoxSakra.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification[binding.checkBoxSakra.text.toString()] = binding.etSakre.text.toString()
            } else {
                userQualification.remove(binding.checkBoxSakra.text.toString())
            }
        }



        binding.checkBoxBup.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification[binding.checkBoxBup.text.toString()] = binding.etBup.text.toString()
            } else {
                userQualification.remove(binding.checkBoxBup.text.toString())
            }
        }

        binding.checkBoxBe.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification[binding.checkBoxBe.text.toString()] = binding.etBe.text.toString()
            } else {
                userQualification.remove(binding.checkBoxBe.text.toString())
            }
        }

        binding.checkBoxSaS.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification[binding.checkBoxSaS.text.toString()] = binding.etSas.text.toString()
            } else {
                userQualification.remove(binding.checkBoxSaS.text.toString())
            }
        }

        binding.checkBoxPlPf.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                userQualification[binding.checkBoxPlPf.text.toString()] = binding.etPlPf.text.toString()
            } else {
                userQualification.remove(binding.checkBoxPlPf.text.toString())
            }
        }



        binding.butBac.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.butRegEnd.setOnClickListener {

            for (key in userQualification.keys){
                if(key == "Hib"){ userQualification[key] = binding.etHiB.text.toString() }
                if(key == "Hip"){ userQualification[key] = binding.etHip.text.toString() }
                if(key == "BE"){ userQualification[key] = binding.etBe.text.toString() }
                if(key == "Sipo"){ userQualification[key] = binding.etSipo.text.toString() }
                if(key == "BM"){ userQualification[key] = binding.etBm.text.toString() }
                if(key == "Sakra"){ userQualification[key] = binding.etSakre.text.toString() }
                if(key == "BuP"){ userQualification[key] = binding.etBup.text.toString() }
                if(key == "PlPf"){ userQualification[key] = binding.etPlPf.text.toString() }
                if(key == "SaS"){ userQualification[key] = binding.etSas.text.toString() }
            }
try {
            fireBase.register(
                userData.email,
                userData.password,
                userData.firstName,
                userData.lastName,
                userData.baNumber,
                userQualification,
            )

    findNavController().navigate(R.id.logInFragment)
}catch (ex: Exception){}


        }

    }
}