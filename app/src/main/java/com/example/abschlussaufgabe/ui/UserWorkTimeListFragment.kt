package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.adapter.UserWorkTimeListAdapter
import com.example.abschlussaufgabe.data.model.UserTestDataModel
import com.example.abschlussaufgabe.databinding.FragmentUserWorkTimeListBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel

val TAG1 = "fragmentWorkTimeList"

/**
 * Fragment, das die Arbeitszeitliste eines Benutzers anzeigt.
 */
class UserWorkTimeListFragment : Fragment() {
    private lateinit var binding: FragmentUserWorkTimeListBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var userData: UserTestDataModel
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    /**
     * Initialisierung vor Erstellung der View.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userData = viewModel.userData
    }

    /**
     * Erstellt die View für das Fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_work_time_list,
            container,
            false
        )
        return binding.root
    }

    /**
     * Wird aufgerufen, nachdem die View erstellt wurde.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Abrufen der Arbeitszeitenliste des Benutzers aus Firestore
        fireStore.getWorkTimeListStore(viewModel.userData.userUid)

        // Beobachtet die Änderungen in der Arbeitszeitenliste und aktualisiert die UI entsprechend
        fireStore.currentTimWorkList.observe(viewLifecycleOwner) {
            binding.rvWorkTime.adapter = UserWorkTimeListAdapter(fireStore._currentTimWorkList.value!!)
            binding.tvListCounter.text = it.size.toString()
            binding.tvHourCounter.text = hourCounter(it)
        }
    }

    /**
     * Zusätzliche Logik bei Wiederaufnahme des Fragments (derzeit leer).
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * Berechnet die gesamte Arbeitszeit in Stunden, Minuten und Sekunden.
     */
    private fun hourCounter(logList: MutableList<String>): String {

        var hour = 0
        var min = 0
        var sek = 0

        // Iteriert durch die Liste und summiert Stunden, Minuten und Sekunden
        for (i in logList) {
            hour += i.split(" ")[6].split(".")[0].toInt()
            min += i.split(" ")[6].split(".")[1].toInt()
            sek += i.split(" ")[6].split(".")[2].toInt()
        }

        var totalSeconds = hour * 3600 + min * 60 + sek

        // Stunden berechnen
        val hoursResult = totalSeconds / 3600
        totalSeconds %= 3600
        // Minuten berechnen
        val minutesResult = totalSeconds / 60
        // Verbleibende Sekunden berechnen
        var secondsResult = totalSeconds % 60

        // Formatierung von Minuten und Sekunden
        val formattedMinutes = if(minutesResult < 10) "0$minutesResult" else "$minutesResult"
        val formattedSeconds = if(secondsResult < 10) "0$secondsResult" else "$secondsResult"

        return "$hoursResult:$formattedMinutes:$formattedSeconds"
    }
}
