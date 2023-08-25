
package com.example.abschlussaufgabe.adapter

import com.example.abschlussaufgabe.data.model.UserTestDataModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.databinding.QualificationItemBinding


class QualificationTestItemAdapter(
    private val dataset: UserTestDataModel
) : RecyclerView.Adapter<QualificationTestItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: QualificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var keysList = dataset.userQualification.keys.toList()
    var valueList = loadValues( keysList, dataset )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            QualificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return keysList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user =
            "${keysList[position]}   ${valueList[position]}"
        holder.binding.tvItemQualification.text = user

    }


    fun loadValues(keysList:List<String>,dataset: UserTestDataModel): List<String>{
        val valueList: MutableList<String> = mutableListOf()
        for (i in keysList) {
            valueList.add(dataset.userQualification[i]!!)
        }
        return valueList
    }
}