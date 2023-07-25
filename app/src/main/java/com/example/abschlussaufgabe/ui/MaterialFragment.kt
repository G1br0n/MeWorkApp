package com.example.abschlussaufgabe.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentMaterialBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel


class MaterialFragment : Fragment() {
    private lateinit var binding: FragmentMaterialBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var userData: UserDataModel

    private var userId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userId = it.getInt("user_id")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView2.text = viewModel.userId.toString()

    }
}