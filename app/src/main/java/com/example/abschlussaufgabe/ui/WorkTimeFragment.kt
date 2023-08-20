package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.QualificationItemAdapter
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel

class WorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentWorkTimeBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //FragmentContainerView 2 von 2 , zuständig für die neben fragmete für start stop fragment, arbeitet mit nav_time
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_time, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}