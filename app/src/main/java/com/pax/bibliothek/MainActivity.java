package com.pax.bibliothek;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Datei: MainActivity.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Launcher-Activity der Bibliothek-App und Einstiegspunkt der Anwendung.
 * Leitet den Benutzer sofort auf die {@link MediumListActivity} weiter und
 * schliesst sich selbst, damit die Zurück-Taste aus der MediumListActivity
 * die App beendet (bleibt nicht im Back-Stack).
 *
 * @author Abdullah M. H.
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Wird beim Start der Activity aufgerufen, leitet sofort zur MediumListActivity
     * weiter und beendet sich selbst.
     *
     * @param savedInstanceState zuvor gespeicherter Zustand der Activity oder {@code null}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Direkt weiterleiten; (mit finish())
        startActivity(new Intent(this, MediumListActivity.class));
        finish();
    }
}