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
import com.example.abschlussaufgabe.databinding.FragmentCarGetInBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * ## Information
 * CarGetInFragment ermöglicht es Benutzern, durch Scannen eines QR-Codes Zugang zu einem Auto zu erhalten.
 */
class CarGetInFragment : Fragment() {

    // Instanz des QR-Code-Scanners.
    private lateinit var codeScanner: CodeScanner
    // Data Binding-Instanz für das zugehörige XML-Layout dieses Fragments.
    private lateinit var binding: FragmentCarGetInBinding
    // Haupt-ViewModel für dieses Fragment.
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * ## Information
     * Wird aufgerufen, um die Benutzeroberfläche für das Fragment zu erstellen.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_car_get_in, container, false)
        return binding.root
    }

    /**
     * ## Information
     * ### Wird aufgerufen, nachdem die Benutzeroberfläche des Fragments erstellt wurde.
     * ### Hier wird der QR-Code-Scanner konfiguriert und verschiedene UI-Interaktionen eingerichtet.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Wenn der Benutzer bereits Zugang zum Auto hat, navigiere direkt zum CarFragment.
        if (viewModel.userData.carStatus) {
            findNavController().navigate(R.id.carFragment)
        }

        // Initialisiere den QR-Code-Scanner.
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()

        var id = 0

        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                try {
                    val text = it.text.toInt()

                    // Spile einen Sound ab, wenn ein QR-Code gescannt wurde.
                    viewModel.playQrSound(context!!)

                    id = text
                    binding.etCarId.text =
                        Editable.Factory.getInstance().newEditable(text.toString())

                    // Bei einem Fehler (z. B. wenn der gescannte Text keine Zahl ist) zeige eine Toast-Nachricht an.
                } catch (ex: Exception) {
                    viewModel.playLockedSound(context!!)
                    Toast.makeText(
                        activity,
                        "Die Id darf nur aus zahlen bestähen",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Starte die Kamera-Vorschau.
        codeScanner.startPreview()

        // Bei einem Klick auf die Scanner-Ansicht, starte die Kamera-Vorschau erneut.
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        // Bei einem Klick auf den "Einsteigen"-Button, aktualisiere den carStatus und navigiere zum CarFragment.
        binding.ibGetIn.setOnClickListener {
            try {
                viewModel.userData.carStatus = true
                findNavController().navigate(R.id.carFragment)
            } catch (ex: Exception) {
                Toast.makeText(activity, "${ex.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * ## Information
     * ### Wird aufgerufen, wenn das Fragment wieder in den Vordergrund tritt.
     * ### Hier wird die Kamera-Vorschau erneut gestartet.
     */
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    /**
     * ## Information
     * ### Wird aufgerufen, wenn das Fragment pausiert wird.
     * ### Hier werden Ressourcen des QR-Code-Scanners freigegeben.
     */
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}