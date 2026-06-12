package com.pax.bibliothek.adapter;

import com.pax.bibliothek.model.Medium;

/**
 * Datei: MediumListener.java
 * Projekt: Bibliothek-App (ÜK Modul 335)
 *
 * Callback-Interface fuer Benutzerinteraktionen in der Medien-Liste.
 * Entkoppelt den MediumAdapter von der konkreten Activity-Implementierung.
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

/**
 * Callback-Interface fÜr Benutzerinteraktionen in der Medien-Liste.
 * Wird von {@link MediumAdapter} aufgerufen und von {@code MediumListActivity} implementiert.
 *
 * @author Abdullah Muhamedu
 */
public interface MediumListener {

    /**
     * Wird aufgerufen, wenn der Benutzer auf ein Medium-Item tippt.
     *
     * @param medium Das angeklickte Medium
     */
    void onItemClick(Medium medium);

    /**
     * Wird aufgerufen, wenn der Benutzer den LÖschen-Button eines Medium-Items tippt.
     *
     * @param medium Das zu lÖschende Medium
     */
    void onDeleteClick(Medium medium);
}
