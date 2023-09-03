package com.example.abschlussaufgabe.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.databinding.UserWorkTimeItemBinding

/**
 * Ein Adapter, um eine Liste von Arbeitszeit-Einträgen in einem RecyclerView anzuzeigen.
 *
 * @param workTimeLogList Liste von Arbeitszeiten als Zeichenketten.
 */
class UserWorkTimeListAdapter(private val workTimeLogList: List<String>): RecyclerView.Adapter<UserWorkTimeListAdapter.ItemViewHolder>() {

    /**
     * ViewHolder für einzelne Einträge im RecyclerView.
     * Hier wird die Datenbindung für die Darstellung des Eintrags genutzt.
     */
    class ItemViewHolder(val binding: UserWorkTimeItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Erstellt einen neuen ViewHolder für den RecyclerView.
     *
     * @param parent Der ViewGroup, in den der neue View eingefügt wird.
     * @param viewType Der View-Typ des neuen Views.
     * @return Ein neuer ViewHolder, der den View für diesen Adapter hält.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = UserWorkTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    /**
     * Verknüpft die Daten des Arbeitszeiteintrags mit dem ViewHolder.
     *
     * @param holder Der ViewHolder, der den Inhalt des Eintrags darstellen soll.
     * @param position Die Position des Eintrags innerhalb der Datenliste.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = workTimeLogList[position]

        // Auszug einiger Dekodierungsbeispiele
        // Datenformat: startGpsLocation stopGpsLocation startWochenTag.jahr.monatValue.tag.stunde.min.sek stopWochenTag.jahr.monatValue.tag.stunde.min.sek Position

        // Dekodiert das Datenformat in einzelne Variablen
        var objectList = item.split(" ")


        // ... Weitere Dekodierungen ...
        var startLatitude = objectList[0]
        var startLongitude = objectList[1]

        // ... Weitere Dekodierungen ...
        var stopLatitude = objectList[2]
        var stopLongitude = objectList[3]

        // ... Weitere Dekodierungen ...
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

        // ... Weitere Dekodierungen ...
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

        // ... Weitere Dekodierungen ...
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

        // ... Weitere Dekodierungen ...
        var position = objectList[7]
        if(position == "Wäle_deine_Position"){
            position = "Keine Position"
        }


        // Setzt die Daten im ViewHolder entsprechend den dekodierten Werten
        holder.binding.tvStartDay.text = "$startWeekDayName $startYear.$startMonth.$startDay"
        holder.binding.tvGps.text = "$startLatitude $startLongitude"
        holder.binding.tvStartTime.text = "$startHour:$startMin:$startSec"
        holder.binding.tvStopTime.text = "$stopHour:$stopMin:$stopSec"
        holder.binding.tvStopDay.text = "$stopWeekDayName $stopYear.$stopMonth.$stopDay"
        holder.binding.tvCounter.text = "$counterHour:$counterMin:$counterSec"
        holder.binding.tvPosition.text = "$position"

    }

    /**
     * Gibt die Gesamtanzahl der Einträge in der Datenliste zurück.
     *
     * @return Anzahl der Einträge in der Datenliste.
     */
    override fun getItemCount(): Int {
        return workTimeLogList.size
    }
}