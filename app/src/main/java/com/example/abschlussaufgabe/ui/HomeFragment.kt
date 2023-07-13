package com.example.abschlussaufgabe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.QualificationItemAdapter
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentHomeBinding
import com.example.abschlussaufgabe.databinding.FragmentLogInBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var userData: UserDataModel
    private val userDataList: List<UserDataModel> = AppRepository().user.value!!

    //todo: arguments user_id
    private var userId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userId = it.getInt("user_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in userDataList){
            if ( i.userId == userId) {
                userData = i
            }
        }

        binding.tvUserName.text = "${userData.userFirstName} ${userData.userLastName}"

        val adapter = QualificationItemAdapter(userData)
        binding.rvQualification.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQualification.adapter = adapter
    }

}