package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.FragmentInWorkTimeBinding
import com.example.abschlussaufgabe.viewmodel.MainViewModel


class InWorkTimeFragment : Fragment() {
    private lateinit var binding: FragmentInWorkTimeBinding

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var userData: UserDataModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_in_work_time, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userData = viewModel.userData

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Spinner ----------------------------------------------------------------------------------
        val positionList = userData.userQualification
        val spinner: Spinner = binding.spinner

        var options = arrayOf("Wäle deine Position")//Optionsliste für dropdownmenü(spinner) mit startwert

        //füle die dropdown list mit list aus UserModel.qwalifikation
        for (i in positionList.indices){
            options =  options.plus(positionList[i])
        }

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, options)
        spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        //------------------------------------------------------------------------------------------

        //TimePicker 24 Hour Style
        binding.myTimePicker.setIs24HourView(true)

        //TODO: Test time picker
        binding.ibStart.setOnClickListener {
        binding.tvSaveCurrentPosition.text = "  ${binding.myTimePicker.hour}:${binding.myTimePicker.minute}"
        }

    }
}