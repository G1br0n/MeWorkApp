package com.example.abschlussaufgabe.ui

import android.annotation.SuppressLint
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
import com.example.abschlussaufgabe.databinding.FragmentMaterialPlacementBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * Dieses Fragment ist verantwortlich für das Platzieren von Materialien, indem es
 * QR-Codes scannt und die entsprechenden Informationen im ViewModel und der Datenbank verarbeitet.
 */
class MaterialPlacementFragment : Fragment() {

    // Deklaration von Klassenvariablen für den QR-Code-Scanner, Datenbindung und ViewModel
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: FragmentMaterialPlacementBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
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
     * Initialisiert UI-Elemente und Ereignishandler, nachdem die Ansicht erstellt wurde.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        var id = 0

        // Initialisierung des QR-Code-Scanners
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                try {
                    // Versucht, den gescannten Text in eine Zahl umzuwandeln
                    val text = it.text.toInt()

                    // Spielt einen Sound ab, wenn ein QR-Code gescannt wurde
                    viewModel.playQrSound(context!!)

                    id = text
                    binding.etMaterialId.text = Editable.Factory.getInstance().newEditable(text.toString())
                } catch (ex: Exception) {
                    // Spielt einen Fehler-Sound ab
                    viewModel.playLockedSound(context!!)
                    Toast.makeText(activity, "Die Id darf nur aus zahlen bestähen", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Startet die Kamera-Vorschau
        codeScanner.startPreview()

        // Event-Listener, um die Kamera-Vorschau erneut zu starten, wenn auf den Scanner geklickt wird
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        // Event-Listener für den Platzierungs-Button
        binding.ibPlace.setOnClickListener {
            try {
                // Extrahiert die ID und SAP-Nummer aus den Eingabefeldern
                id = binding.etMaterialId.text.toString().toInt()
                val sapNumber = binding.etSapNumber.text.toString().toInt()

                // Überprüft, ob die ID in der Liste ist
                viewModel.checkMaterialId(id)

                // Aktualisiert die Position des Materials in der Datenbank
                viewModel.updateMaterialLocation(id, sapNumber.toString())

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
     * Wird aufgerufen, wenn das Fragment fortgesetzt wird.
     */
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    /**
     * Wird aufgerufen, wenn das Fragment pausiert wird.
     */
    override fun onPause() {
        // Gibt Ressourcen des Scanners frei
        codeScanner.releaseResources()
        super.onPause()
    }
}
