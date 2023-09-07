package com.example.abschlussaufgabe.ui

// Import-Anweisungen für die benötigten Klassen und Pakete
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
import com.example.abschlussaufgabe.databinding.FragmentMaterialDeliverBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * ## Information
 * Ein Fragment, das dem Benutzer erlaubt, Material durch das Scannen eines QR-Codes abzugeben.
 */
class MaterialDeliverFragment : Fragment() {

    // Klassenvariablen für den Datenbindung und das ViewModel
    private lateinit var binding: FragmentMaterialDeliverBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material_deliver, container, false)
        return binding.root
    }

    /**
     * ## Information
     * Initialisiert UI-Elemente und Ereignishandler, nachdem die Ansicht erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Extrahiert die ID aus dem QR scaner in TextView
        viewModel.getQrCodeScan(view,activity!!,context!!,binding.etMaterialId)

        // Startet die Kamera-Vorschau
        viewModel.codeScanner.startPreview()

        // Wird aufgerufen, wenn der Scanner-Bereich angeklickt wird
        binding.scannerView.setOnClickListener {
            viewModel.codeScanner.startPreview()
        }

        // Wird aufgerufen, wenn der Abgabe-Button angeklickt wird
        binding.ibDeliver.setOnClickListener {
            try {
                viewModel.checkMaterialId(binding.etMaterialId.text.toString().toInt())
                viewModel.updateMaterialLocation(binding.etMaterialId.text.toString().toInt(), "1")
                viewModel.playActionSound(context!!)
                Toast.makeText(activity, "Material erfolgreich im Lager Abgegeben", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.materialFragment)
            } catch (ex: Exception) {
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
