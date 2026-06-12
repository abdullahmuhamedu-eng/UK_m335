package com.pax.bibliothek.adapter;

import com.pax.bibliothek.model.Medium;

/**
 * Datei: MediumListener.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Callback-Interface für Benutzerinteraktionen in der Medien-Liste.
 * Entkoppelt den MediumAdapter von der konkreten Activity-Implementierung.
 * Wird von {@link MediumAdapter} aufgerufen und von {@code MediumListActivity} implementiert.
 *
 * @author Abdullah M. H.
 * @version 1.0
 */
public interface MediumListener {

    /**
     * Wird aufgerufen, wenn der Benutzer auf ein Medium-Item tippt.
     *
     * @param medium Das angeklickte Medium
     */
    void onItemClick(Medium medium);

    /**
     * Wird aufgerufen, wenn der Benutzer den Löschen-Button eines Medium-Items tippt.
     *
     * @param medium Das zu löschende Medium
     */
    void onDeleteClick(Medium medium);
}