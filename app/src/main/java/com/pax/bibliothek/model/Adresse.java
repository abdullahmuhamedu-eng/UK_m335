package com.pax.bibliothek.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Datei: Adresse.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Datenmodell für eine Postadresse. Wird als eingebettetes Objekt
 * im Kunde-DTO vom Backend übertragen und per Gson deserialisiert.
 * Implementiert {@link Serializable}, damit Adresse-Objekte via Intent übergeben werden können.
 *
 * @author Matthias
 * @author Abdullah M. H.
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

    public Adresse(String strasse, String ort) {
        this.strasse = strasse;
        this.ort = ort;
    }

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