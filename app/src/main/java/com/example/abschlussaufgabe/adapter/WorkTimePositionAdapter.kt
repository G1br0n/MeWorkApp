import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.abschlussaufgabe.databinding.FragmentInWorkTimeBinding

class WorkTimePositionAdapter(context: Context, private val itemList: List<String>) :
    ArrayAdapter<String>(context, 0, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)


        val binding = convertView?.let { FragmentInWorkTimeBinding.bind(it) }
            ?: FragmentInWorkTimeBinding.inflate(LayoutInflater.from(context), parent, false)

        val spinner: Spinner = binding.spinner
        val options = arrayOf(itemList)


        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, options
        )

        spinner.adapter = adapter

        return binding.root
    }
}