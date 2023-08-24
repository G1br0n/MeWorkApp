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
import com.example.abschlussaufgabe.databinding.FragmentLogInBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel


class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Login button wen er angeklickt wird

        binding.button.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)

        }


        binding.ibLogin.setOnClickListener {

            //Empfange eigegebenen text
            val inputUsername = binding.ettLogIn.text.toString()
            val inputPassword = binding.ettPassword.text.toString()

            var isValid = false


            //Hier durchsuche ich die userdaten und gleiche es mit eingegebenen daten im loginfragment ab
            for (user in viewModel.userDataList.value!!) {
                if (user.userLogIn.lowercase() == inputUsername.lowercase() && user.userPassword == inputPassword) {
                    isValid = true
                    viewModel.userData = user
                    break
                }

            }



            if (isValid) {
                //Naviegire zum homeFragment wenn die userdaten ubereinstimmen
                viewModel.playLogInSound(context!!)
                Toast.makeText(requireContext(), "Sie haben sich erfolgreich Angemeldet", Toast.LENGTH_LONG).show()
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())

            } else {
                //Wenn die userdaten nicht ind der liste sind oder der eingegebene passwort nicht übereinstimt wirt eine Toast nachricht dem entsprechendnangezeigt
               viewModel.playLockedSound(context!!)
               // Toast.makeText(requireContext(), "Passwort oder Username ist falsch", Toast.LENGTH_LONG).show()
            }

        }
    }


    override fun onResume() {
        super.onResume()
        //Schlisse navBar wenn loginfragment wieder geöfnet wird
        (activity as? MainActivity)?.closeNavigationBar()
    }
}