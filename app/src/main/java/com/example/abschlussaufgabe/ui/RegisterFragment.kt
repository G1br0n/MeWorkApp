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

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageView3.setImageResource(R.drawable.avatar_logo)

        binding.butBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.butNext.setOnClickListener {
           try {

           fireStore.userData = updateUser(fireStore.userData)
            findNavController().navigate(R.id.registerQualificationFragment)
           } catch (ex:Exception){
               Toast.makeText(requireContext(), "Unerwartete feller", Toast.LENGTH_LONG).show()

           }

        }


    }

    fun updateUser(user:UserTestDataModel): UserTestDataModel{
        user.email = binding.etEmail.text.toString()
        user.password = binding.etPassword.text.toString()
        user.firstName = binding.etVorname.text.toString()
        user.lastName = binding.etName.text.toString()
        user.baNumber = binding.etBa.text.toString().toInt()
        user.userQualification = mapOf()
        return user
    }


}