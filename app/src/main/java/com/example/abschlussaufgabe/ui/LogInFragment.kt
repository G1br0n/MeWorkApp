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
 * ## Information
 * ### LogInFragment repräsentiert ein Benutzeroberflächen-Element, in dem sich Benutzer anmelden können.
 * ### Es bietet auch die Möglichkeit, zum Registrierungsbildschirm zu navigieren.
 */
class LogInFragment : Fragment() {

    // Verwendet Data Binding, um die XML-Layout-Datei mit dieser Klasse zu verknüpfen.
    private lateinit var binding: FragmentLogInBinding

    // Instanzen von ViewModels für verschiedene Zwecke.
    private val viewModel: MainViewModel by activityViewModels()
    private val fireBase: FireBaseAuthViewModel by activityViewModels()
    private val fireStore: FireStoreViewModel by activityViewModels()

    /**
     * ## Information
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
     * ## Information
     * Konfiguriert UI-Interaktionen und -Logik nach der Erstellung der Ansicht.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Event-Listener für den Registrierungsbutton, um zum Registrierungs-Fragment zu navigieren.
        binding.button.setOnClickListener {
            viewModel.playClickSound(context!!)
            findNavController().navigate(R.id.registerFragment)
        }


        // Event-Listener für den Login-Button.
        binding.ibLogin.setOnClickListener {
            try {
                // Spielt einen Sound ab, wenn der Button geklickt wird.
                viewModel.playClickSound(context!!)

                // Überprüft den Internetstatus und wirft eine Exception, wenn keine Verbindung besteht.
                viewModel.internetCheck(context!!)

                // Liest den Benutzernamen und das Passwort aus den Eingabefeldern und entfernt dabei Leerzeichen.
                val inputUsername = binding.ettLogIn.text.toString().replace(" ", "")
                val inputPassword = binding.ettPassword.text.toString().replace(" ", "")

                // Überprüft die Gültigkeit der E-Mail-Adresse und wirft eine Exception bei einem ungültigen Format.
                viewModel.isValidEmail(inputUsername)

                // Überprüft das Passwort auf Gültigkeit und wirft eine Exception, wenn es nicht den Anforderungen entspricht.
                viewModel.isValidPassword(inputPassword)

                // Zeigt eine kurze Toast-Nachricht an, die den Benutzer über den Start des Login-Vorgangs informiert.
                Toast.makeText(activity, "Login Check", Toast.LENGTH_SHORT).show()

                // Ändert die Sichtbarkeit des Login-Buttons und des Ladebalkens, um den Login-Vorgang anzuzeigen.
                binding.ibLogin.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE

                // Startet den Login-Vorgang mit der Firebase-Authentifizierung.
                fireBase.login(inputUsername, inputPassword)

                // Überwacht den aktuellen Benutzerstatus in Firebase.
                fireBase.currentUserBase.observe(viewLifecycleOwner) { user ->
                    user?.let {
                        // Speichert die Benutzer-ID im ViewModel.
                        viewModel.userData.userUid = it.uid

                        // Lädt zusätzliche Benutzerdaten und Arbeitszeiten aus der Firestore-Datenbank.
                        fireStore.getUserDataStore(it.uid)
                        fireStore.getWorkTimeListStore(it.uid)

                        // Überwacht Änderungen in den Firestore-Benutzerdaten.
                        fireStore.currentUserStore.observe(viewLifecycleOwner) { data ->
                            data?.let {
                                // Bei Änderungen der Benutzerdaten werden die Arbeitszeiten erneut aus Firestore geladen.
                                fireStore.getWorkTimeListStore(user.uid)

                                // Lädt zusätzliche Materialien für den Benutzer.
                                viewModel.loadUserMaterialList()

                                // Informiert den Benutzer über den erfolgreichen Login.
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

                    // Wenn nach 10 Sekunden kein erfolgreicher Login festgestellt wurde, informiert der Benutzer darüber.
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
                    }, 10000)
                }
            } catch (ex: Exception) {
                viewModel.playLockedSound(context!!)
                // Zeigt eine Fehlermeldung an, wenn während des Login-Vorgangs eine Ausnahme auftritt.
                Toast.makeText(
                    activity,
                    ex.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * ## Information
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