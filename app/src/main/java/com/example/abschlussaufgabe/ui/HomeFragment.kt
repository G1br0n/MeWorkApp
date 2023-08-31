package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.MaterialItemAdapter
import com.example.abschlussaufgabe.adapter.QualificationTestItemAdapter
import com.example.abschlussaufgabe.databinding.FragmentHomeBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager


const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
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
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUserMaterialList()

        //Weise den FlexBox LayoutManager für material recyclerview
        binding.rvMaterial.layoutManager = FlexboxLayoutManager(requireContext())


        //Observer für UserDaten aus dem fireStore
        fireStore.currentUserStore.observe(viewLifecycleOwner) {
            binding.tvUserName.text = "${it.firstName} ${it.lastName}"
            binding.tvUserBa.text = "BA-${it.baNumber}"
            binding.tvUserBup.text = "BüP-${it.baNumber}"
            binding.rvQualification.adapter = QualificationTestItemAdapter(it)
        }

        //Observe material liste aus dem RoomDatabase
        viewModel.userMaterialList.observe(viewLifecycleOwner) {
            Log.e("Home", "$it")
            binding.rvMaterial.adapter = MaterialItemAdapter(it)
        }

        //Fange mögliche API NullPointerException ab
        try {
            //Beobachte die geladen Api liste
            viewModel.bfPhotoList.observe(viewLifecycleOwner) {
                if (it.title != null) {
                    binding.tvCityTitle.text = "    ${it.title}    "
                }
                if (it.photoUrl != null) {
                    binding.imageView2.load(it.photoUrl)
                }
            }

            binding.imageView2.setOnClickListener {
                viewModel.playClickSound(context!!)
                viewModel.loadBfPhotoList()
            }
        } catch (ex: Exception) {
            //Fange unbekante feller ab
            Log.e(TAG, "ImageViewSetOnClick: ${ ex.message!! }")
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.loadBfPhotoList()
    }
}