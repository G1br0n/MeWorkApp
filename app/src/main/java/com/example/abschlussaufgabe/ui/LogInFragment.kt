package com.example.abschlussaufgabe.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussaufgabe.MainActivity
import com.example.abschlussaufgabe.R
import com.example.abschlussaufgabe.databinding.FragmentLogInBinding
import com.example.abschlussaufgabe.viewmodel.FireBaseAuthViewModel
import com.example.abschlussaufgabe.viewmodel.FireStoreViewModel
import com.example.abschlussaufgabe.viewmodel.MainViewModel

/**
 * LogInFragment repräsentiert ein Benutzeroberflächen-Element, in dem sich Benutzer anmelden können.
 * Es bietet auch die Möglichkeit, zum Registrierungsbildschirm zu navigieren.
 */
class LogInFragment : Fragment() {

    // Verwendet Data Binding, um die XML-Layout-Datei mit dieser Klasse zu verknüpfen.
    private lateinit var binding: FragmentLogInBinding

    // Instanzen von ViewModels für verschiedene Zwecke.
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    /**
     * Wird aufgerufen, um die Benutzeroberfläche für das Fragment zu erstellen.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Benutzer wird bei jedem Laden des Fragments abgemeldet.
        fireBase.logout()

        // Initialisiert die Datenbindung für das Fragment.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false)
        return binding.root
    }

    /**
     * Konfiguriert UI-Interaktionen und -Logik nach der Erstellung der Ansicht.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Event-Listener für den Registrierungsbutton, um zum Registrierungs-Fragment zu navigieren.
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        // Event-Listener für den Login-Button.
        binding.ibLogin.setOnClickListener {
            // Spielt einen Sound ab, wenn der Button geklickt wird.
            viewModel.playClickSound(context!!)
            // Zeigt eine kurze Toast-Nachricht an, die den Benutzer über den Login-Vorgang informiert.
            Toast.makeText(activity, "Login Check", Toast.LENGTH_SHORT).show()

            // Liest den Benutzernamen und das Passwort aus den Eingabefeldern.
            val inputUsername = binding.ettLogIn.text.toString()
            val inputPassword = binding.ettPassword.text.toString()

            // Ändert die Sichtbarkeit des Login-Buttons und des Ladebalkens während des Login-Vorgangs.
            binding.ibLogin.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            // Startet den Login-Vorgang mit Firebase.
            fireBase.login(inputUsername, inputPassword)

            // Überwacht den aktuellen Benutzerstatus in Firebase.
            fireBase.currentUserBase.observe(viewLifecycleOwner) { user ->
                user?.let {
                    viewModel.userData.userUid = it.uid
                    // Lädt zusätzliche Benutzerdaten und Arbeitszeiten aus Firestore.
                    fireStore.getUserDataStore(it.uid)
                    fireStore.getWorkTimeListStore(it.uid)

                    // Überwacht Änderungen in den Firestore-Benutzerdaten.
                    fireStore.currentUserStore.observe(viewLifecycleOwner) { data ->
                        data?.let {
                            fireStore.getWorkTimeListStore(user.uid)
                            viewModel.loadUserMaterialList()
                            Toast.makeText(
                                activity,
                                "Anmeldung war erfolgreich",
                                Toast.LENGTH_LONG
                            ).show()
                            // Navigiert zum Haupt-Fragment nach erfolgreichem Login.
                            findNavController().navigate(R.id.homeFragment)
                        }
                    }
                }

                // Wenn nach 5 Sekunden kein Login-Erfolg erkannt wurde, wird eine Fehlermeldung angezeigt.
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!fireBase.checkedLogin) {
                        binding.ibLogin.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            activity,
                            "Email oder Passwort falsch",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, 5000)
            }
        }
    }

    /**
     * Konfiguriert bestimmte UI-Elemente, wenn das Fragment wieder im Vordergrund ist.
     */
    override fun onResume() {
        super.onResume()

        // Loggt den Benutzer erneut aus, wenn das Fragment wieder aufgerufen wird.
        fireBase.logout()

        // Schließt die Navigationsleiste, wenn das LoginFragment erneut geöffnet wird.
        (activity as? MainActivity)?.closeNavigationBar()
    }
}