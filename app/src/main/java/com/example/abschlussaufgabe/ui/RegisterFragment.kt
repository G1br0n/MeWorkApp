package com.example.abschlussaufgabe.ui

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.example.abschlussaufgabe.databinding.FragmentRegisterBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    val cameraPackageName = "com.example.camera"
    private val CAMERA_REQUEST_CODE = 100

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            binding.imageView3.setImageBitmap(imageBitmap)
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*

        binding.imageView3.setImageResource(R.drawable.avatar_logo)


            binding.imageView3.setOnClickListener {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
                } else {
                    openSpecificCameraApp()
                }
            }
*/



        binding.butBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.butNext.setOnClickListener {
           try {

           fireStore.userData = updateUser(fireStore.userData)
            findNavController().navigate(R.id.registerQualificationFragment)
           } catch (ex:Exception){
               Toast.makeText(requireContext(), "Unerwartete feller", Toast.LENGTH_LONG).show()

           }

        }


    }

    fun updateUser(user:UserTestDataModel): UserTestDataModel{
        user.email = binding.etEmail.text.toString()
        user.password = binding.etPassword.text.toString()
        user.firstName = binding.etVorname.text.toString()
        user.lastName = binding.etName.text.toString()
        user.baNumber = binding.etBa.text.toString().toInt()
        user.userQualification = mapOf()
        return user
    }

  /*  private fun openSpecificCameraApp() {
        val cameraPackageName = "com.example.camera"  // Ersetzen Sie dies durch den tatsächlichen Paketnamen der Kamera-App

        val intent = requireContext().packageManager.getLaunchIntentForPackage(cameraPackageName)
        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Kamera-App nicht gefunden", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(requireContext(), "Keine Kamera-App gefunden", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openSpecificCameraApp()
            } else {
                Toast.makeText(requireContext(), "Kamera-Berechtigung wurde verweigert", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}