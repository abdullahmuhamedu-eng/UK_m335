package com.pax.bibliothek.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Datei: Adresse.java
 * Projekt: Bibliothek-App (üK Modul 335)
 *
 * Datenmodell fuer eine Postadresse. Wird als eingebettetes Objekt
 * im Kunde-DTO vom Backend uebertragen und per Gson deserialisiert.
 *
 *
 * Bean-Klasse fÜr eine Postadresse (Kundenadresse).
 * Implementiert {@link Serializable}, damit Adresse-Objekte via Intent Übergeben werden kÖnnen.
 *
 * @author Matthias
 * @author Abdullah Muhamedu
 * @version 1.0
 */
public final class Adresse implements Serializable {

    private static final long serialVersionUID = 2375134275131779804L;

    @SerializedName("id")      private long id;
    @SerializedName("strasse") private String strasse;
    @SerializedName("ort")     private String ort;
    @SerializedName("plz")     private String plz;

    /** Kein-Argument-Konstruktor fuer Jackson-Deserialisierung. */
    Adresse() {}

    /**
     * C'tor zum Erstellen einer neuen Adresse mit den Pflichtfeldern.
     *
     * @param strasse Strassenname inkl. Hausnummer
     * @param ort     Ortsname
     */
    public Adresse(String strasse, String ort) {
        this.strasse = strasse;
        this.ort = ort;
    }

    /**
     * C'tor zum Erstellen einer neuen Adresse mit allen Feldern.
     *
     * @param strasse Strassenname inkl. Hausnummer
     * @param ort     Ortsname
     * @param plz     Postleitzahl
     */
    public Adresse(String strasse, String ort, String plz) {
        this.strasse = strasse;
        this.ort = ort;
        this.plz = plz;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getStrasse() { return strasse; }

    public String getOrt() { return ort; }

    public String getPlz() { return plz; }
    public void setPlz(String plz) { this.plz = plz; }
}
