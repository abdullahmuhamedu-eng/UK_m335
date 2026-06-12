package com.pax.bibliothek.adapter;

import com.pax.bibliothek.model.Ausleihe;

/**
 * Datei: AusleiheListener.java
 * Projekt: Bibliothek-App (ÜK Modul 335)
 *
 * Callback-Interface fuer Benutzerinteraktionen in der Ausleihen-Liste.
 * Entkoppelt den AusleiheAdapter von der konkreten Activity-Implementierung.
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

/**
 * Callback-Interface für Benutzerinteraktionen in der Ausleihen-Liste.
 * Wird von {@link AusleiheAdapter} aufgerufen und von {@code AusleiheListActivity} implementiert.
 *
 * @author Abdullah Muhamedu
 */
public interface AusleiheListener {

    /**
     * Wird aufgerufen, wenn der Benutzer auf eine Ausleihe-Zeile tippt.
     *
     * @param ausleihe Die angeklickte Ausleihe
     */
    void onItemClick(Ausleihe ausleihe);

    /**
     * Wird aufgerufen, wenn der Benutzer den Löschen-Button einer Ausleihe-Zeile tippt.
     *
     * @param ausleihe Die zu löschende Ausleihe (entspricht Rueckgabe des Mediums)
     */
    void onDeleteClick(Ausleihe ausleihe);
}
