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


class CarGetInFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: FragmentCarGetInBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_car_get_in, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        if (viewModel.userData.carStatus){
            findNavController().navigate(R.id.carFragment)
        }


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
                    binding.etCarId.text =
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
        binding.ibGetIn.setOnClickListener {
            try {

                viewModel.userData.carStatus = true
                findNavController().navigate(R.id.carFragment)



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