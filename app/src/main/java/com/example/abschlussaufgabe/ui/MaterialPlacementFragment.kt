package com.example.abschlussaufgabe.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentMaterialPlacementBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * ## Information
 * ### Dieses Fragment ist verantwortlich für das Platzieren von Materialien, indem es
 * ### QR-Codes scannt und die entsprechenden Informationen im ViewModel und der Datenbank verarbeitet.
 */
class MaterialPlacementFragment : Fragment() {

    // Deklaration von Klassenvariablen für den QR-Code-Scanner, Datenbindung und ViewModel
    private lateinit var binding: FragmentMaterialPlacementBinding
    private val viewModel: MainViewModel by activityViewModels()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material_placement, container, false)
        return binding.root
    }

    /**
     * ## Information
     * Initialisiert UI-Elemente und Ereignishandler, nachdem die Ansicht erstellt wurde.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Extrahiert die ID aus dem QR scaner in TextView
        viewModel.getQrCodeScan(view,activity!!,context!!,binding.etMaterialId)

        // Startet die Kamera-Vorschau
        viewModel.codeScanner.startPreview()

        // Wird aufgerufen, wenn der Scanner-Bereich angeklickt wird
        binding.scannerView.setOnClickListener {
            viewModel.codeScanner.startPreview()
        }

        // Event-Listener für den Platzierungs-Button
        binding.ibPlace.setOnClickListener {
            try {
                // Extrahiert AP-Nummer aus den Eingabefeldern
                val sapNumber = binding.etSapNumber.text.toString().toInt()

                // Überprüft, ob die ID in der Liste ist
                viewModel.checkMaterialId(binding.etMaterialId.text.toString().toInt())

                // Aktualisiert die Position des Materials in der Datenbank
                viewModel.updateMaterialLocation(binding.etMaterialId.text.toString().toInt(), sapNumber.toString())

                // Lädt die Benutzer-Materialliste aus der Datenbank
                viewModel.loadUserMaterialList()

                // Spielt einen Bestätigungs-Sound ab
                viewModel.playActionSound(context!!)

                // Informiert den Benutzer über die erfolgreiche Platzierung
                Toast.makeText(activity, "Material erfolgreich platziert", Toast.LENGTH_LONG).show()

                // Navigiert zurück zum MaterialFragment
                findNavController().navigate(R.id.materialFragment)
            } catch (ex: Exception) {
                // Informiert den Benutzer über Fehler
                Toast.makeText(activity, "${ex.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * ## Information
     * Wird aufgerufen, wenn das Fragment fortgesetzt wird.
     */
    override fun onResume() {
        super.onResume()
        viewModel.codeScanner.startPreview()
    }

    /**
     * ## Information
     * Wird aufgerufen, wenn das Fragment pausiert wird.
     */
    override fun onPause() {
        // Gibt Ressourcen des Scanners frei
        viewModel.codeScanner.releaseResources()
        super.onPause()
    }
}
