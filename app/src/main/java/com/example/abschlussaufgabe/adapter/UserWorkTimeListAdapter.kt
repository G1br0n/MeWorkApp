package com.example.abschlussaufgabe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.databinding.UserWorkTimeItemBinding

class UserWorkTimeListAdapter(private val workTimeLogList: List<String>): RecyclerView.Adapter<UserWorkTimeListAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: UserWorkTimeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = UserWorkTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = workTimeLogList[position]




    }

    override fun getItemCount(): Int {
        return workTimeLogList.size
    }
}