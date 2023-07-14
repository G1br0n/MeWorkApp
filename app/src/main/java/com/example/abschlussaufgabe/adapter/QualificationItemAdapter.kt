package com.example.abschlussaufgabe.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.QualificationItemBinding


class QualificationItemAdapter(
    private val dataset: UserDataModel
) : RecyclerView.Adapter<QualificationItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: QualificationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = QualificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  dataset.userQualification.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = "${dataset.userQualification[position]}   ${dataset.userQualificationFit[position].year}.${dataset.userQualificationFit[position].monthValue}.${dataset.userQualificationFit[position].dayOfMonth}"
        holder.binding.tvItemQualification.text = user

    }
}