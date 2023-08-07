package com.example.abschlussaufgabe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.UserMaterialDatabase
import com.example.abschlussaufgabe.databinding.ActivityMainBinding
import com.example.abschlussaufgabe.databinding.FragmentLogInBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel


class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //TODO 1: LogIn dataBank
        binding.ibLogin.setOnClickListener {
            val inputUsername = binding.ettLogIn.text.toString()
            val inputPassword = binding.ettPassword.text.toString()

            var isValid = false

            for (user in viewModel.userList.value!!) {
                if (user.userLogIn.lowercase() == inputUsername.lowercase() && user.userPassword == inputPassword) {
                    isValid = true
                    viewModel.userData = user
                    break
                }
            }


            if (isValid) {
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
                (activity as? MainActivity)?.showNavigationBar()

            } else {
                Toast.makeText(
                    requireContext(),
                    "Eingegebene Passwort oder Username sind falsch",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}