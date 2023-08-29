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
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel


class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fireBase.logout()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Regestrirungs Processe button
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)

        }

        //Login button
        binding.ibLogin.setOnClickListener {
            //Interacktion sound
            viewModel.playClickSound(context!!)
            //Interacktion mesage
            Toast.makeText(activity, "Login Check", Toast.LENGTH_SHORT).show()

            //Empfange eigegebenen text
            val inputUsername = binding.ettLogIn.text.toString()
            val inputPassword = binding.ettPassword.text.toString()

            //loge mich bei fireBase an
            fireBase.login(inputUsername, inputPassword)

            //beobachte die input von fireBase dan lade ich fireStore userData
            fireBase.currentUserBase.observe(viewLifecycleOwner) {
                if (it != null) {

                    //lade userTestDataModel aus fireStoreData
                    fireStore.getUserDataStore(it.uid)

                    //beobachte die input von fireStore dan naviegire ich weiter
                    fireStore.currentUserStore.observe(viewLifecycleOwner) { data ->
                        if (data != null) {
                            Toast.makeText(activity, "Anmeldung war erfolgreich", Toast.LENGTH_LONG)
                                .show()
                            findNavController().navigate(R.id.homeFragment)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        fireBase.logout()

        //Schlisse navBar wenn loginfragment wieder geöfnet wird
        (activity as? MainActivity)?.closeNavigationBar()
    }
}