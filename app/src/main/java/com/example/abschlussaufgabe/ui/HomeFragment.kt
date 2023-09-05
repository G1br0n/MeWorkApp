package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.MaterialItemAdapter
import com.example.abschlussaufgabe.adapter.QualificationTestItemAdapter
import com.example.abschlussaufgabe.databinding.FragmentHomeBinding
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel
import com.google.android.flexbox.FlexboxLayoutManager


const val TAG = "HomeFragment"
/**
 * ## Information
 * HomeFragment repräsentiert den Hauptbildschirm der App nachdem sich der Benutzer angemeldet hat.
 * Es zeigt verschiedene Benutzerinformationen sowie Materialien und Qualifikationstests an.
 */
class HomeFragment : Fragment() {

    // Data Binding-Instanz für das zugehörige XML-Layout dieses Fragments.
    private lateinit var binding: FragmentHomeBinding

    // ViewModels, die für verschiedene Daten- und UI-Operationen innerhalb dieses Fragments verwendet werden.
    private val viewModel: MainViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    /**
     * ## Information
     * Wird aufgerufen, wenn das Fragment zum ersten Mal erstellt wird.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Zeigt die Navigationsleiste an, wenn zum HomeFragment navigiert wird.
        (activity as? MainActivity)?.showNavigationBar()
    }

    /**
     * ## Information
     * Wird aufgerufen, um die Benutzeroberfläche für das Fragment zu erstellen.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialisiert die Datenbindung für dieses Fragment.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    /**
     * ## Information
     * ### Wird aufgerufen, nachdem die Benutzeroberfläche des Fragments erstellt wurde.
     * ### Hier werden Daten von Firestore abgerufen und verschiedene UI-Elemente konfiguriert.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Benutzerdaten aus Firestore abrufen.
        fireStore.getUserDataStore(viewModel.userData.userUid)

        // FlexboxLayoutManager wird für den RecyclerView der Materialien festgelegt.
        binding.rvMaterial.layoutManager = FlexboxLayoutManager(requireContext())

        // Observer für Benutzerdaten aus Firestore. Hier werden UI-Elemente mit den abgerufenen Daten aktualisiert.
        fireStore.currentUserStore.observe(viewLifecycleOwner) {
            binding.tvUserName.text = "${it.firstName} ${it.lastName}"
            binding.tvUserBa.text = "BA-${it.baNumber}"
            binding.tvUserBup.text = "BüP-${it.baNumber}"
            binding.rvQualification.adapter = QualificationTestItemAdapter(it)
        }

        // Observer für die Materialliste aus der Room-Datenbank. Der RecyclerView wird mit den Materialdaten aktualisiert.
        viewModel.userMaterialList.observe(viewLifecycleOwner) {
            Log.e("Home", "$it")
            binding.rvMaterial.adapter = MaterialItemAdapter(it)
        }

        try {
            // Beobachtet die Liste der Fotos, die von einer API geladen wurden.
            viewModel.bfPhotoList.observe(viewLifecycleOwner) {
                if (it.title != null) {
                    binding.tvCityTitle.text = "    ${it.title}    "
                }
                if (it.photoUrl != null) {
                    // Lädt und zeigt das Foto aus der URL mit Hilfe der Coil-Bibliothek an.
                    binding.imageView2.load(it.photoUrl)
                }
            }

            // OnClickListener für das ImageView-Element.
            binding.imageView2.setOnClickListener {
                viewModel.playClickSound(context!!)
                viewModel.loadBfPhotoList()
            }
        } catch (ex: Exception) {
            // Loggt Fehler, die während des Setzens des OnClickListeners für das ImageView auftreten können.
            Log.e(TAG, "ImageViewSetOnClick: ${ ex.message!! }")
        }
    }
}