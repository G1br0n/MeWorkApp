package com.example.abschlussaufgabe.ui

import MaterialItemAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.QualificationItemAdapter
import com.example.abschlussaufgabe.data.AppRepository
import com.example.abschlussaufgabe.data.local.UserMaterialDatabase
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentHomeBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var userData: UserDataModel


    //todo: arguments user_id
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userData = viewModel.userData
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

        //CardView User
        binding.tvUserName.text = "${userData.userFirstName} ${userData.userLastName}"
        binding.tvUserBa.text = "BA-${userData.userBaNumber}"
        binding.tvUserBup.text = "BüP-${userData.userBupNumber}"

        //RecyclerView for Qualification
        binding.rvQualification.layoutManager = LinearLayoutManager(requireContext())

        viewModel.userList.observe(viewLifecycleOwner){ _ ->
            binding.rvQualification.adapter = QualificationItemAdapter(userData)
        }

        //CardView Material user
        binding.rvMaterial.layoutManager = FlexboxLayoutManager(requireContext())
        viewModel.userMaterialList.observe(viewLifecycleOwner){
            binding.rvMaterial.adapter = MaterialItemAdapter(it)

        }

    }

}