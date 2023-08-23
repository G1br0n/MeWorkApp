package com.example.abschlussaufgabe.ui

import MaterialItemAdapter
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentMaterialReceivedBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel


class MaterialReceivedFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: FragmentMaterialReceivedBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_material_received, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()

        var id = 0

        //QR Code decoder
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                //löse bewust feller aus wenn die eingegebene ID kein zahl ist
                try {
                    val text = it.text.toInt()

                    //Spile sound ab wenn  qr geskent wurde
                    viewModel.playQrSound(context!!)


                    id = text
                    binding.etMaterialId.text =
                        Editable.Factory.getInstance().newEditable(text.toString())
                    //Fange den feller ab mit eine toast nachricht
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

        //starte camera preview
        codeScanner.startPreview()

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        //Button empfangen wen er angeklickt  wird
        binding.ibReceived.setOnClickListener {
            try {
                // setze id aus dem eingabe feld
                id = binding.etMaterialId.text.toString().toInt()

                //Überprüfe mit der funktion ob Id in der liste ist oder nicht, wen nicht schmeise feller raus
                viewModel.checkMaterialId(id)

                //udate StorageMaterial Model Datenbank
                viewModel.updateMaterialLocation(id, viewModel.userData.userId.toInt())

                //Spile sound ab wenn  material empfange
                viewModel.playActionSound(context!!)

                //Benachrichtige user über die erfolgreiche action
                Toast.makeText(activity, "Material erfolgreich empfangen", Toast.LENGTH_LONG).show()

                //Naviegire zurück zu MaterialFragment
                findNavController().navigate(R.id.materialFragment)
                //Fange Mögliche Feller ab
            } catch (ex: Exception) {
                //Benachritige mit Toast über den feller
                Toast.makeText(activity, "${ex.message}", Toast.LENGTH_LONG).show()
            }


        }


    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}