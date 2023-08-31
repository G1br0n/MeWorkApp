package com.example.abschlussaufgabe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.databinding.MaterialItemBinding


class MaterialItemAdapter(private val itemList: List<StorageMaterialModel>) : RecyclerView.Adapter<MaterialItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: MaterialItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = MaterialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.textView.text = item.name
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}
