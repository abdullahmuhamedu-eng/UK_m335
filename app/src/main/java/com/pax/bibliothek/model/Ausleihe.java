package com.pax.bibliothek.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Datei: Ausleihe.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Bean-Klasse für eine Medienausleihe. Verknüpft einen Kunden mit einem Medium
 * und speichert Leihdatum sowie Leihdauer. Das Fälligkeitsdatum wird client-seitig
 * aus Leihdatum und Leihdauer berechnet.
 * Implementiert {@link Serializable}, damit Objekte via Intent übergeben werden können.
 *
 * @author Abdullah M. H.
 * @version 1.0
 */
public final class Ausleihe implements Serializable {

    private static final long serialVersionUID = 2425672164760554627L;

    @SerializedName("id")        private long id;
    @SerializedName("leihdatum") private Date leihdatum;
    @SerializedName("leihdauer") private Short leihdauer;
    @SerializedName("kunde")     private Kunde kunde;
    @SerializedName("medium")    private Medium medium;

    /** Kein-Argument-Konstruktor fuer Jackson-Deserialisierung. */
    Ausleihe() {}

    public Ausleihe(long kundenNummer, long inventarNummer) {
        this.kunde = new Kunde(kundenNummer);
        this.medium = new Medium(inventarNummer);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Date getLeihdatum() { return leihdatum; }
    public void setLeihdatum(Date leihdatum) { this.leihdatum = leihdatum; }

    public Short getLeihdauer() { return leihdauer; }

    public void setLeihdauer(Short leihdauer) { this.leihdauer = leihdauer; }

    public Kunde getKunde() { return kunde; }

    public Medium getMedium() { return medium; }

    /**
     * Gibt das Leihdatum als lokalisierte Zeichenkette aus.
     *
     * @return Leihdatum im lokalen Datumsformat oder leerer String wenn nicht gesetzt
     */
    public String getLeihdatumLokalisiert() {
        if (leihdatum == null) return "";
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        return format.format(leihdatum);
    }

    /**
     * Berechnet und gibt das Fälligkeitsdatum als lokalisierte Zeichenkette aus.
     * Das Fälligkeitsdatum ergibt sich aus Leihdatum + Leihdauer Tage.
     * Diese Eigenschaft ist transient und wird bei jedem Aufruf neu berechnet.
     *
     * @return Fälligkeitsdatum im lokalen Datumsformat oder leerer String wenn Leihdatum fehlt
     */
    public String getFaelligkeitsdatumLokalisiert() {
        if (leihdatum == null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(leihdatum);
        // Leihdauer addieren; wenn null, bleibt das Leihdatum unveraendert
        if (leihdauer != null) cal.add(Calendar.DAY_OF_YEAR, leihdauer);
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        return format.format(cal.getTime());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Ausleihe [id=" + id + ", leihdatum=" + getLeihdatumLokalisiert()
                + ", faellig=" + getFaelligkeitsdatumLokalisiert()
                + ", kunde=" + kunde + ", medium=" + medium + "]";
    }
}