package com.example.abschlussaufgabe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.QualificationItemBinding
import com.example.abschlussaufgabe.ui.HomeFragment

class QualificationItemAdapter(
    private val dataset: List<UserDataModel>
) : RecyclerView.Adapter<QualificationItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: QualificationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = QualificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  dataset[0].userQualification.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = dataset[0].userQualification[position]
        holder.binding.tvItemQualification.text = user

    }
}