package com.example.abschlussaufgabe.ui

// Importe der notwendigen Bibliotheken und Klassen
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
import com.example.abschlussaufgabe.databinding.FragmentMaterialReceivedBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * Dieses Fragment ist für das Scannen von QR-Codes zuständig und
 * verarbeitet die gescannte Information, um das Material zu verwalten.
 */
class MaterialReceivedFragment : Fragment() {

    // Deklaration von Mitgliedervariablen für den CodeScanner, Datenbindung und ViewModel
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: FragmentMaterialReceivedBinding
    private val viewModel: MainViewModel by activityViewModels()

    /**
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
     * Wird aufgerufen, um die Benutzeroberfläche des Fragments zu erstellen, nachdem die
     * View erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        var id = 0

        // Initialisierung des QR-Code-Scanners
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                try {
                    // Versuche, den gescannten Text in eine Zahl umzuwandeln
                    val text = it.text.toInt()

                    // Abspielen eines Sounds, wenn QR-Code gescannt wurde
                    viewModel.playQrSound(context!!)

                    id = text
                    // Setzen des gescannten Texts in ein Eingabefeld
                    binding.etMaterialId.text = Editable.Factory.getInstance().newEditable(text.toString())
                } catch (ex: Exception) {
                    // Abspielen eines Fehler-Sounds
                    viewModel.playLockedSound(context!!)

                    // Anzeigen einer Fehlermeldung, wenn der gescannte Code nicht nur Zahlen enthält
                    Toast.makeText(activity, "Die Id darf nur aus zahlen bestähen", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Starten der Kamera-Vorschau
        codeScanner.startPreview()

        // Listener, um die Kamera-Vorschau erneut zu starten, wenn auf die Ansicht geklickt wird
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        // Listener für den "Empfangen"-Button
        binding.ibReceived.setOnClickListener {
            try {
                // Holen der ID aus dem Eingabefeld
                id = binding.etMaterialId.text.toString().toInt()

                // Überprüfung, ob die ID in einer Liste vorhanden ist
                viewModel.checkMaterialId(id)

                // Aktualisierung der Materialposition in der Datenbank
                viewModel.updateMaterialLocation(id, viewModel.userData.userUid)

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
     * Wird aufgerufen, wenn das Fragment fortgesetzt wird.
     */
    override fun onResume() {
        super.onResume()
        // Fortsetzen der Kamera-Vorschau, wenn das Fragment fortgesetzt wird
        codeScanner.startPreview()
    }

    /**
     * Wird aufgerufen, wenn das Fragment pausiert wird.
     */
    override fun onPause() {
        // Freigeben von Ressourcen, wenn das Fragment pausiert wird
        codeScanner.releaseResources()
        super.onPause()
    }
}
