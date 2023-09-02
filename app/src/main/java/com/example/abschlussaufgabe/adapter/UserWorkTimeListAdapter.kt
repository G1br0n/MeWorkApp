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

        //startGpsLocation stopGpsLocation startWochenTag.jahr.monatValue.tag.stunde.min.sek stopWochenTag.jahr.monatValue.tag.stunde.min.sek Position

        //dekoder
        var objectList = item.split(" ")

        var startLatitude = objectList[0]
        var startLongitude = objectList[1]

        var stopLatitude = objectList[2]
        var stopLongitude = objectList[3]

        var startWeekDayName = objectList[4].split(".")[0]
        var startYear = objectList[4].split(".")[1]
        var startMonth = objectList[4].split(".")[2]
        var startDay = objectList[4].split(".")[3]
        var startHour = objectList[4].split(".")[4]
        if(startHour.toList().size < 2){
            startHour = ("0$startHour")
        }
        var startMin = objectList[4].split(".")[5]
        if(startMin.toList().size < 2){
            startMin = ("0$startMin")
        }
        var startSec = objectList[4].split(".")[6]
        if(startSec.toList().size < 2){
            startSec = ("0$startSec")
        }

        var stopWeekDayName = objectList[5].split(".")[0]
        var stopYear = objectList[5].split(".")[1]
        var stopMonth = objectList[5].split(".")[2]
        var stopDay = objectList[5].split(".")[3]
        var stopHour = objectList[5].split(".")[4]
        if(stopHour.toList().size < 2){
            stopHour = ("0$stopHour")
        }
        var stopMin = objectList[5].split(".")[5]
        if(stopMin.toList().size < 2){
            stopMin = ("0$stopMin")
        }
        var stopSec = objectList[5].split(".")[6]
        if(stopSec.toList().size < 2){
            stopSec = ("0$stopSec")
        }


        var counterHour = objectList[6].split(".")[0]
        if(counterHour.toList().size < 2){
            counterHour = ("0$counterHour")
        }
        var counterMin = objectList[6].split(".")[1]
        if(counterMin.toList().size < 2){
            counterMin = ("0$counterMin")
        }
        var counterSec = objectList[6].split(".")[2]
        if(counterSec.toList().size < 2){
            counterSec = ("0$counterSec")
        }


        var position = objectList[7]
        if(position == "Wäle_deine_Position"){
            position = "Keine Position"
        }



        holder.binding.tvStartDay.text = "$startWeekDayName $startYear.$startMonth.$startDay"
        holder.binding.tvGps.text = "$startLatitude $startLongitude"
        holder.binding.tvStartTime.text = "$startHour:$startMin:$startSec"
        holder.binding.tvStopTime.text = "$stopHour:$stopMin:$stopSec"
        holder.binding.tvStopDay.text = "$stopWeekDayName $stopYear.$stopMonth.$stopDay"
        holder.binding.tvCounter.text = "$counterHour:$counterMin:$counterSec"
        holder.binding.tvPosition.text = "$position"

    }

    override fun getItemCount(): Int {
        return workTimeLogList.size
    }
}