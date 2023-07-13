package com.example.abschlussaufgabe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.databinding.FragmentLogInBinding


class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding

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
        val test: String = "test"

        binding.ibLogin.setOnClickListener {
            val inputUsername = binding.ettLogIn.text.toString()
            val inputPassword = binding.ettPassword.text.toString()

            var isValid = false

            for (user in AppRepository().user.value!!) {
                if (user.userLogIn == inputUsername && user.userPassword == inputPassword) {
                    isValid = true
                    break
                }
            }

            if (isValid) {
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
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