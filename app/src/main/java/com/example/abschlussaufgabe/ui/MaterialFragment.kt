package com.example.abschlussaufgabe.ui


import MaterialItemAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentMaterialBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager


class MaterialFragment : Fragment() {
    private lateinit var binding: FragmentMaterialBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var userData: UserDataModel
    private var userId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userData = viewModel.userData
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvMaterial.layoutManager = FlexboxLayoutManager(requireContext())
        viewModel.materialList.observe(viewLifecycleOwner){
            binding.rvMaterial.adapter = MaterialItemAdapter(it)
        }

        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.materialReceivedFragment)
        }
    }
}