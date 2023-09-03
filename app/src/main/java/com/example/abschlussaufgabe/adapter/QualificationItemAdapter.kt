package com.example.abschlussaufgabe.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.data.model.UserDataModel
import com.example.abschlussaufgabe.databinding.QualificationItemBinding

/**
 * Ein Adapter, der Qualifikationseinträge in einem RecyclerView anzeigt.
 *
 * @param dataset Das Datenmodell, das die Benutzerqualifikationen enthält.
 */
class QualificationItemAdapter(private val dataset: UserDataModel) : RecyclerView.Adapter<QualificationItemAdapter.ItemViewHolder>() {

    /**
     * ViewHolder für einzelne Einträge im RecyclerView.
     * Hier wird die Datenbindung für die Darstellung des Eintrags genutzt.
     */
    class ItemViewHolder(val binding: QualificationItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Erstellt einen neuen ViewHolder für den RecyclerView.
     *
     * @param parent Der ViewGroup, in den der neue View eingefügt wird.
     * @param viewType Der View-Typ des neuen Views.
     * @return Ein neuer ViewHolder, der den View für diesen Adapter hält.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Inflate (erzeugen) des Layouts für den Eintrag mithilfe von Datenbindung
        val binding = QualificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    /**
     * Gibt die Gesamtanzahl der Einträge in der Datenliste zurück.
     *
     * @return Anzahl der Einträge in der Benutzerqualifikationsliste.
     */
    override fun getItemCount(): Int {
        return dataset.userQualification.size
    }

    /**
     * Verknüpft die Daten des Qualifikationseintrags mit dem ViewHolder.
     *
     * @param holder Der ViewHolder, der den Inhalt des Eintrags darstellen soll.
     * @param position Die Position des Eintrags innerhalb der Datenliste.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Formatierung des angezeigten Texts für den Eintrag
        val user = "${dataset.userQualification[position]}   ${dataset.userQualificationFit[position].year}.${dataset.userQualificationFit[position].month}.${dataset.userQualificationFit[position].day}"
        // Zuweisen des formatierten Texts zum TextView des ViewHolder
        holder.binding.tvItemQualification.text = user
    }
}
