package com.pax.bibliothek;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Datei: MainActivity.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Einstiegspunkt der Android-App. Leitet den Benutzer sofort auf
 * die Medienliste weiter und schliesst sich selbst, damit die
 * Zurueck-Taste aus der MediumListActivity die App beendet.
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

/**
 * Launcher-Activity der Bibliothek-App.
 * Dient ausschliesslich als Weiterleitungspunkt zur {@link MediumListActivity}
 * und bleibt nicht im Back-Stack.
 *
 * @author Abdullah Muhamedu
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Direkt weiterleiten; finish() verhindert, dass diese Activity im Back-Stack bleibt
        startActivity(new Intent(this, MediumListActivity.class));
        finish();
    }
}
