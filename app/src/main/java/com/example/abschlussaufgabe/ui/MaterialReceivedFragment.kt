package com.example.abschlussaufgabe.ui

import android.graphics.Color
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
import com.example.abschlussaufgabe.data.local.StorageMaterialDatabase
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material_received, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        val storageMaterial = viewModel.storageMaterialDatabase.storageMaterialDao
        var id = 1

        //QR Code decoder
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                //löse bewust feller aus wenn die eingegebene ID kein zahl ist
                try {
                    val text = it.text.toInt()
                    id = text
                    binding.etMaterialId.text = Editable.Factory.getInstance().newEditable(text.toString())
                //Fange den feller ab mit eine toast nachricht
                } catch(ex: Exception) {
                    Toast.makeText(activity, "Die Id darf nur aus zahlen bestähen", Toast.LENGTH_LONG).show()
                }
            }

        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }


        binding.ibReceived.setOnClickListener{

            try {

                id = binding.etMaterialId.text.toString().toInt()

                var test = viewModel.storageMaterialDatabase.storageMaterialDao.getById(1).value!!.materialId
                //test!!.locationId = 1001
                //viewModel.hallo.value!!.locationId = 1001

                Toast.makeText(activity, "${test}", Toast.LENGTH_LONG).show()

                viewModel.updateMaterialLocation(1,1001)

                viewModel.loadUserMaterialList()
            } catch (ex: Exception) {
                Toast.makeText(activity, "${ex.message}", Toast.LENGTH_LONG).show()
            }

            findNavController().navigateUp()
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