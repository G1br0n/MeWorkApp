package com.example.abschlussaufgabe.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.MaterialItemAdapter
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.example.abschlussaufgabe.databinding.FragmentMaterialBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * ## Information
 * Dieses Fragment zeigt eine Liste von Materialien an und bietet dem Benutzer verschiedene
 * Optionen zur Interaktion mit diesen Materialien.
 */
class MaterialFragment : Fragment() {

    // Deklaration von Klassenvariablen für Datenbindung, ViewModels und Benutzerdaten
    private lateinit var binding: FragmentMaterialBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var userData: UserTestDataModel
    private val fireBase: FireBaseAuthViewModel by activityViewModels()

    /**
     * ## Information
     * Wird aufgerufen, wenn das Fragment erstmals erstellt wird.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Die Benutzerdaten aus dem ViewModel werden geladen.
        userData = viewModel.userData
    }

    /**
     * ## Information
     * Erzeugt die Benutzeroberfläche des Fragments.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialisierung der Datenbindung
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false)
        return binding.root
    }

    /**
     * ## Information
     * Initialisiert UI-Elemente und Ereignishandler, nachdem die Ansicht erstellt wurde.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setzt den LayoutManager für die RecyclerViews
        binding.rvMaterial.layoutManager = FlexboxLayoutManager(requireContext())
        binding.rvMaterialA.layoutManager = FlexboxLayoutManager(requireContext())

        // Beobachtet die Liste der Materialien und aktualisiert die Adapter entsprechend
        viewModel.userMaterialList.observe(viewLifecycleOwner) {
            binding.rvMaterial.adapter = MaterialItemAdapter(it)
        }
        viewModel.userPsaMaterialList.observe(viewLifecycleOwner) {
            Log.e("Mat","binding.rvMaterialA.adapter")
            binding.rvMaterialA.adapter = MaterialItemAdapter(it)
        }

        // Event-Listener für die verschiedenen Interaktions-Buttons
        binding.ibMaterialReciver.setOnClickListener {
            viewModel.playClickSound(context!!)
            findNavController().navigate(R.id.materialReceivedFragment)
        }
        binding.ibMaterialDeliver.setOnClickListener {
            viewModel.playClickSound(context!!)
            findNavController().navigate(R.id.materialDeliverFragment)
        }
        binding.ibMaterialPlacement.setOnClickListener {
            viewModel.playClickSound(context!!)
            findNavController().navigate(R.id.materialPlacementFragment)
        }
        // Inaktive Schaltflächen spielen einen "gesperrt" Sound ab
        binding.ibMaterialHandOver.setOnClickListener {
            viewModel.playLockedSound(context!!)
        }
        binding.imageButton5.setOnClickListener {
            // In diesem Moment spielt der Button keinen Sound, kann aber in der Zukunft verwendet werden
        }
        binding.imageButton6.setOnClickListener {
            // Der Benutzer wird ausgeloggt und zur Anmeldeseite navigiert
            fireBase.logout()
            findNavController().navigate(R.id.logInFragment)
        }

        // Lädt die Benutzer-Materialliste
        viewModel.loadUserMaterialList()
    }

}
