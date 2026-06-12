package com.pax.bibliothek.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Datei: RetrofitFactory.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Singleton-Fabrik für die Retrofit-Instanz.
 * Konfiguriert Gson mit korrektem Datumsformat für das Backend.
 *
 * @author Abdullah M. H.
 * @version 1.0
 */
public class RetrofitFactory {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.193:8080/";

    /**
     * Gibt die Retrofit-Instanz zurück (Singleton).
     * Erzeugt sie beim ersten Aufruf mit konfiguriertem Gson-Konverter.
     *
     * @return Konfigurierte Retrofit-Instanz
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}