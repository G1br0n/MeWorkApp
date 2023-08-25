package com.example.abschlussaufgabe.ui

import MaterialItemAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.QualificationTestItemAdapter
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.example.abschlussaufgabe.databinding.FragmentHomeBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var userData: UserTestDataModel
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()



    //todo: arguments user_id
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        //Öfne NavBar bei navigiren zum homeFragment
        (activity as? MainActivity)?.showNavigationBar()
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

        userData = fireStore.currentUserStore.value!!

        //CardView User
        binding.tvUserName.text = "${userData.firstName} ${userData.lastName}"
        //binding.tvUserName.text = "${viewModel.storageMaterialDataList.value!![0].name}"
        binding.tvUserBa.text = "BA-${userData.baNumber}"
        binding.tvUserBup.text = "BüP-${userData.baNumber}"

        //RecyclerView for Qualification
        binding.rvQualification.layoutManager = LinearLayoutManager(requireContext())

        viewModel.userDataList.observe(viewLifecycleOwner){ _ ->
            binding.rvQualification.adapter = QualificationTestItemAdapter(userData)
        }

        //CardView Material user
        binding.rvMaterial.layoutManager = FlexboxLayoutManager(requireContext())

        viewModel.userMaterialList.observe(viewLifecycleOwner){
            Log.e("Home","$it")
            binding.rvMaterial.adapter = MaterialItemAdapter(it)

        }


        viewModel.bfPhotoList.observe(viewLifecycleOwner){
            binding.tvCityTitle.text = "    ${it.title}    "
            if(it.photoUrl != null){
                binding.imageView2.load(it.photoUrl)
            }
        }

        binding.imageView2.setOnClickListener {
            viewModel.loadBfPhotoList()
        }




    }

    override fun onResume() {
        super.onResume()
      // viewModel.loadUserMaterialList()
        viewModel.loadBfPhotoList()
    }
}