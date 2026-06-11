package com.pax.bibliothek.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Datei: Kunde.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Datenmodell fuer einen Bibliothekskunden. Wird vom Backend als JSON geliefert
 * und per Gson in dieses Java-Objekt deserialisiert.
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

/**
 * Bean-Klasse fuer einen Bibliothekskunden.
 * Enthaelt Personendaten sowie eine eingebettete {@link Adresse}.
 * Implementiert {@link Serializable}, damit Objekte via Intent uebergeben werden koennen.
 *
 * @author Abdullah Muhamedu
 */
public final class Kunde implements Serializable {

    private static final long serialVersionUID = 2177739324279157094L;

    @SerializedName("id")           private long id;
    @SerializedName("vorname")      private String vorname;
    @SerializedName("familienname") private String familienname;
    @SerializedName("geburtsdatum") private Date geburtsdatum;
    @SerializedName("adresse")      private Adresse adresse;
    @SerializedName("email")        private String email;

    /** Kein-Argument-Konstruktor fuer Jackson-Deserialisierung. */
    Kunde() {}

    /**
     * C'tor zum Erstellen eines Kunden-Stubs mit gesetzter Kundennummer.
     * Wird intern fuer Ausleihe-Referenzen verwendet.
     *
     * @param id Kundennummer
     */
    Kunde(long id) {
        this.id = id;
    }

    /**
     * C'tor zum Erstellen eines neuen Kunden mit allen Pflichtfeldern.
     *
     * @param vorname      Vorname
     * @param familienname Familienname
     * @param geburtsdatum Geburtsdatum
     * @param adresse      Wohnadresse
     * @param email        E-Mail-Adresse
     */
    public Kunde(String vorname, String familienname, Date geburtsdatum, Adresse adresse, String email) {
        this.vorname = vorname;
        this.familienname = familienname;
        this.geburtsdatum = geburtsdatum;
        this.adresse = adresse;
        this.email = email;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getVorname() { return vorname; }

    public String getFamilienname() { return familienname; }
    public void setFamilienname(String familienname) { this.familienname = familienname; }

    /**
     * Gibt das Geburtsdatum als lokalisierte Zeichenkette aus.
     *
     * @return Geburtsdatum im lokalen Datumsformat oder leerer String wenn nicht gesetzt
     */
    public String getGeburtsdatumLokalisiert() {
        if (geburtsdatum == null) return "";
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        return format.format(geburtsdatum);
    }

    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Kunde [id=" + id + ", name=" + vorname + " " + familienname
                + ", geburtsdatum=" + getGeburtsdatumLokalisiert() + "]";
    }
}
