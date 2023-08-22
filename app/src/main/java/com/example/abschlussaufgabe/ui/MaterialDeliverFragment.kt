package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.databinding.FragmentMaterialDeliverBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class MaterialDeliverFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: FragmentMaterialDeliverBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_material_deliver, container, false)
        return binding.root
    }

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
                    val materialId = it.text.toInt()

                    //Spile sound ab wenn  qr geskent wurde
                    viewModel.playQrSound(context!!)

                    id = materialId
                    binding.etMaterialId.text =
                        Editable.Factory.getInstance().newEditable(materialId.toString())
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


        //Button abgeben wen er angeklickt  wird
        binding.ibDeliver.setOnClickListener {
            try {

                //hol id in variable aus dem eingabe feld im UI
                id = binding.etMaterialId.text.toString().toInt()

                //Überprüfemit der funktion ob Id in der liste ist oder nicht, wen nicht schmeise feller raus
                viewModel.checkMaterialId(id)

                //udate StorageMaterial Model Datenbank
                viewModel.updateMaterialLocation(id, 1)

                //Spile sound ab wenn  material empfange
                viewModel.playActionSound(context!!)

                //Benachrichtige user über die erfolgreiche action
                Toast.makeText(activity, "Material erfolgreich im Lager Abgegeben", Toast.LENGTH_LONG).show()

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