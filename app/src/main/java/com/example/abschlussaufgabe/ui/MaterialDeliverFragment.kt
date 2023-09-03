package com.example.abschlussaufgabe.ui

// Import-Anweisungen für die benötigten Klassen und Pakete
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentMaterialDeliverBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * ## Information
 * Ein Fragment, das dem Benutzer erlaubt, Material durch das Scannen eines QR-Codes abzugeben.
 */
class MaterialDeliverFragment : Fragment() {

    // Klassenvariablen für den QR-Code-Scanner, Datenbindung und das ViewModel
    private lateinit var codeScanner: CodeScanner
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

        // Initialisiert den QR-Code-Scanner
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        var id = 0

        codeScanner = CodeScanner(activity, scannerView)

        // Setzt den Dekodierungs-Callback für den Scanner
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                try {
                    // Versucht, die gescannte ID in eine Zahl zu konvertieren
                    val materialId = it.text.toInt()
                    viewModel.playQrSound(context!!)

                    id = materialId
                    // Setzt den Text des Eingabefelds auf die gescannte ID
                    binding.etMaterialId.text = Editable.Factory.getInstance().newEditable(materialId.toString())
                } catch (ex: Exception) {
                    // Spielt einen Fehlersound ab, wenn der gescannte Text keine gültige Zahl ist
                    viewModel.playLockedSound(context!!)
                    Toast.makeText(activity, "Die Id darf nur aus zahlen bestähen", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Startet die Kamera-Vorschau
        codeScanner.startPreview()

        // Wird aufgerufen, wenn der Scanner-Bereich angeklickt wird
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        // Wird aufgerufen, wenn der Abgabe-Button angeklickt wird
        binding.ibDeliver.setOnClickListener {
            try {
                id = binding.etMaterialId.text.toString().toInt()
                viewModel.checkMaterialId(id)
                viewModel.updateMaterialLocation(id, "1")
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
        codeScanner.startPreview()
    }

    /**
     * ## Information
     * Wird aufgerufen, wenn das Fragment pausiert wird.
     */
    override fun onPause() {
        // Gibt Ressourcen des Scanners frei
        codeScanner.releaseResources()
        super.onPause()
    }
}
