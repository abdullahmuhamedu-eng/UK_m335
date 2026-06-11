package com.pax.bibliothek.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Datei: RetrofitFactory.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Factory-Klasse fuer die Retrofit-Instanz. Stellt sicher, dass nur eine
 * einzige Retrofit-Instanz (Singleton) in der gesamten App verwendet wird.
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

/**
 * Singleton-Factory fuer Retrofit. Kapselt die Konfiguration der HTTP-Bibliothek
 * (Base-URL und Gson-Konverter) und gibt immer dieselbe Instanz zurueck.
 *
 * Verwendung:
 *   Retrofit r = RetrofitFactory.getRetrofitInstance();
 *   BibliothekProxy proxy = r.create(BibliothekProxy.class);
 *
 * @author Abdullah Muhamedu
 */
public class RetrofitFactory {

    private static Retrofit retrofit;

    // IP-Adresse und Port des Spring-Boot-Backends (lokales Netzwerk, laut Aufgabenstellung)
    private static final String BASE_URL = "http://192.168.1.193:8080/";

    /**
     * Gibt die Singleton-Instanz von Retrofit zurueck.
     * Erstellt die Instanz beim ersten Aufruf (Lazy Initialization) mit Gson-Konverter.
     *
     * @return Konfigurierte Retrofit-Instanz
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
