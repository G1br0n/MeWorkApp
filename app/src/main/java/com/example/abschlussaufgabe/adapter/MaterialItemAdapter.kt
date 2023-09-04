package com.example.abschlussaufgabe.adapter

// Notwendige Importanweisungen für Android-Layout-Infrastruktur und Datenmodell
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.data.model.StorageMaterialModel
import com.example.abschlussaufgabe.databinding.MaterialItemBinding

/**
 * ## Information
 * Ein Adapter für das RecyclerView, der Materialien aus einem Lager in der App darstellt.
 *
 * @param itemList Eine Liste von Material-Objekten, die angezeigt werden sollen.
 */
class MaterialItemAdapter(private val itemList: List<StorageMaterialModel>) : RecyclerView.Adapter<MaterialItemAdapter.ItemViewHolder>() {

    /**
     * ## Information
     * Ein ViewHolder, der für die Darstellung eines einzelnen Material-Objekts in der Liste verantwortlich ist.
     * Er verwendet das MaterialItemBinding für die Datenbindung, um die Darstellung des Objekts zu steuern.
     */
    class ItemViewHolder(val binding: MaterialItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * ## Information
     * Erzeugt einen neuen ViewHolder, der für die Darstellung eines einzelnen Eintrags im RecyclerView verantwortlich ist.
     *
     * @param parent Der ViewGroup, in den der neue View eingefügt wird.
     * @param viewType Der View-Typ des neuen Views.
     * @return Ein neuer ViewHolder, der den View für diesen Adapter hält.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Erzeugen des Layouts für den Material-Eintrag durch Datenbindung
        val binding = MaterialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    /**
     * ## Information
     * Befüllt den ViewHolder mit Daten aus der Liste basierend auf der angegebenen Position.
     *
     * @param holder Der ViewHolder, der den Inhalt des Eintrags anzeigen soll.
     * @param position Die Position des Eintrags in der Liste.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Holen des Material-Objekts an der gegebenen Position
        val item = itemList[position]
        // Setzen des Namens des Materials im TextView des ViewHolders
        holder.binding.textView.text = item.name
    }

    /**
     * ## Information
     * Gibt die Gesamtanzahl der Einträge in der Liste zurück.
     *
     * @return Anzahl der Einträge in der Materialliste.
     */
    override fun getItemCount(): Int {
        return itemList.size
    }
}
