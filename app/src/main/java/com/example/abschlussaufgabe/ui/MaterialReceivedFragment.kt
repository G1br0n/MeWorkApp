package com.example.abschlussaufgabe.ui

// Importe der notwendigen Bibliotheken und Klassen
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
import com.budiyev.android.codescanner.CodeScanner
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentMaterialReceivedBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * ## Information
 * Dieses Fragment ist für das Scannen von QR-Codes zuständig und
 * verarbeitet die gescannte Information, um das Material zu verwalten.
 */
class MaterialReceivedFragment : Fragment() {

    // Deklaration von Mitgliedervariablen für den CodeScanner, Datenbindung und ViewModel
    private lateinit var binding: FragmentMaterialReceivedBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * ## Information
     * Wird aufgerufen, um die Benutzeroberfläche des Fragments zu erstellen.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialisierung der Datenbindung
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material_received, container, false)
        return binding.root
    }

    /**
     * ## Information
     * Wird aufgerufen, um die Benutzeroberfläche des Fragments zu erstellen, nachdem die
     * View erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Extrahiert die ID aus dem QR scaner in TextView
        var id = viewModel.getQrCodeScan(view,activity!!,context!!,binding.etMaterialId)

        // Startet die Kamera-Vorschau
        viewModel.codeScanner.startPreview()

        // Wird aufgerufen, wenn der Scanner-Bereich angeklickt wird
        binding.scannerView.setOnClickListener {
            viewModel.codeScanner.startPreview()
        }

        // Listener für den "Empfangen"-Button
        binding.ibReceived.setOnClickListener {
            try {

                // Überprüfung, ob die ID in einer Liste vorhanden ist
                viewModel.checkMaterialId(id.toInt())

                // Aktualisierung der Materialposition in der Datenbank
                viewModel.updateMaterialLocation(id.toInt(), viewModel.userData.userUid)

                // Abspielen eines Aktions-Sounds
                viewModel.playActionSound(context!!)

                // Benachrichtigen des Benutzers über den erfolgreichen Vorgang
                Toast.makeText(activity, "Material erfolgreich empfangen", Toast.LENGTH_LONG).show()

                // Navigation zurück zum MaterialFragment
                findNavController().navigate(R.id.materialFragment)
            } catch (ex: Exception) {
                // Anzeigen einer Fehlermeldung
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
        // Fortsetzen der Kamera-Vorschau, wenn das Fragment fortgesetzt wird
        viewModel.codeScanner.startPreview()
    }

    /**
     * ## Information
     * Wird aufgerufen, wenn das Fragment pausiert wird.
     */
    override fun onPause() {
        // Freigeben von Ressourcen, wenn das Fragment pausiert wird
        viewModel.codeScanner.releaseResources()
        super.onPause()
    }
}
