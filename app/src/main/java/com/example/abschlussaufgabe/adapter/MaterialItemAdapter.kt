
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussaufgabe.adapter.QualificationItemAdapter

import com.example.abschlussaufgabe.data.model.MaterialModel
import com.example.abschlussaufgabe.databinding.MaterialItemBinding
import com.example.abschlussaufgabe.databinding.QualificationItemBinding

class MaterialItemAdapter(private val itemList: List<MaterialModel>) : RecyclerView.Adapter<MaterialItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: MaterialItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialItemAdapter.ItemViewHolder {
        val binding = MaterialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialItemAdapter.ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.textView.text = item.name

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}
