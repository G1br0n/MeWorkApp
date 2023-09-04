package com.example.abschlussaufgabe.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.example.abschlussaufgabe.databinding.QualificationItemBinding

/**
 * ## Information
 * Ein Adapter, um Qualifikationstestdaten in einem RecyclerView darzustellen.
 *
 * @param dataset Datenmodell, das Benutzerqualifikationen enthält.
 */
class QualificationTestItemAdapter(
    private val dataset: UserTestDataModel
) : RecyclerView.Adapter<QualificationTestItemAdapter.ItemViewHolder>() {

    /**
     * ## Information
     * ViewHolder für einzelne Einträge im RecyclerView.
     * Hier wird die Datenbindung für die Darstellung des Eintrags genutzt.
     */
    class ItemViewHolder(val binding: QualificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Erstellt Listen der Schlüssel und zugehörigen Werte aus den Qualifikationsdaten.
    var keysList = dataset.userQualification.keys.toList()
    var valueList = loadValues(keysList, dataset)

    /**
     * ## Information
     * Erstellt einen neuen ViewHolder für den RecyclerView.
     *
     * @param parent Der ViewGroup, in den der neue View eingefügt wird.
     * @param viewType Der View-Typ des neuen Views.
     * @return Ein neuer ViewHolder, der den View für diesen Adapter hält.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = QualificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    /**
     * ## Information
     * Gibt die Gesamtanzahl der Einträge in der Datenliste zurück.
     *
     * @return Anzahl der Einträge in der Datenliste.
     */
    override fun getItemCount(): Int {
        return keysList.size
    }

    /**
     * ## Information
     * Verknüpft die Daten des Qualifikationseintrags mit dem ViewHolder.
     *
     * @param holder Der ViewHolder, der den Inhalt des Eintrags darstellen soll.
     * @param position Die Position des Eintrags innerhalb der Datenliste.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = "${keysList[position]}   ${valueList[position]}"
        holder.binding.tvItemQualification.text = user
    }

    /**
     * ## Information
     * Hilfsfunktion, um die Werte aus dem Benutzerqualifikationsdatensatz zu laden.
     *
     * @param keysList Liste von Schlüsseln, für die Werte geladen werden sollen.
     * @param dataset Datenmodell, das die Benutzerqualifikationen enthält.
     * @return Liste von Werten, die den gegebenen Schlüsseln entsprechen.
     */
    fun loadValues(keysList:List<String>,dataset: UserTestDataModel): List<String> {
        val valueList: MutableList<String> = mutableListOf()
        for (i in keysList) {
            valueList.add(dataset.userQualification[i]!!)
        }
        return valueList
    }
}