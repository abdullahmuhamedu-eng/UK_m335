package com.pax.bibliothek.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Datei: Medium.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Datenmodell fuer ein Bibliotheksmedium (Buch, DVD, etc.).
 * Wird vom Backend als JSON geliefert und per Gson in dieses Java-Objekt deserialisiert.
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

/**
 * Bean-Klasse fuer ein Bibliotheksmedium (Buch, DVD, etc.).
 * Die Inventarnummer entspricht der Datenbank-ID.
 * Implementiert {@link Serializable}, damit Objekte via Intent uebergeben werden koennen.
 *
 * @author Abdullah Muhamedu
 */
public final class Medium implements Serializable {

    private static final long serialVersionUID = 1881561894623524513L;

    @SerializedName("id")       private long id;
    @SerializedName("titel")    private String titel;
    @SerializedName("autor")    private String autor;
    @SerializedName("genre")    private String genre;
    @SerializedName("fsk")      private Short fsk;
    @SerializedName("ean")      private Long ean;
    @SerializedName("standort") private String standort;

    /** Kein-Argument-Konstruktor fuer Jackson-Deserialisierung. */
    Medium() {}

    /**
     * C'tor zum Erstellen eines Medium-Stubs mit gesetzter Inventarnummer.
     * Wird intern fuer Ausleihe-Referenzen verwendet.
     *
     * @param id Inventarnummer
     */
    Medium(long id) {
        this.id = id;
    }

    /**
     * C'tor zum Erstellen eines neuen Mediums mit den Pflichtfeldern.
     *
     * @param titel Titel des Mediums
     * @param autor Autor des Mediums
     */
    public Medium(String titel, String autor) {
        this.titel = titel;
        this.autor = autor;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitel() { return titel; }

    public String getAutor() { return autor; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Short getFsk() { return fsk; }
    public void setFsk(Short fsk) { this.fsk = fsk; }

    public Long getEan() { return ean; }
    public void setEan(Long ean) { this.ean = ean; }

    public String getStandort() { return standort; }
    public void setStandort(String standort) { this.standort = standort; }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Medium [id=" + id + ", titel=" + titel + ", autor=" + autor + ", genre=" + genre + "]";
    }
}
