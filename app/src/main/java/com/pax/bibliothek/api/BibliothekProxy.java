package com.pax.bibliothek.api;

import com.pax.bibliothek.model.Medium;
import com.pax.bibliothek.model.Ausleihe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Datei: BibliothekProxy.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Retrofit-Interface fuer das REST-API der Bibliothek.
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */
public interface BibliothekProxy {

    /** GET http://192.168.1.193:8080/bibliothek/medien */
    @GET("bibliothek/medien")
    Call<List<Medium>> getAllMedien();

    /** POST http://192.168.1.193:8080/bibliothek/medium (neu) */
    @POST("bibliothek/medium")
    Call<Medium> addMedium(@Body Medium medium);

    /** POST http://192.168.1.193:8080/bibliothek/medium (mit ID im Body = Update) */
    @POST("bibliothek/medium")
    Call<Medium> updateMedium(@Body Medium medium);

    /** DELETE http://192.168.1.193:8080/bibliothek/medium/{id} */
    @DELETE("bibliothek/medium/{id}")
    Call<Void> deleteMedium(@Path("id") long id);

    /** GET http://192.168.1.193:8080/bibliothek/ausleihen */
    @GET("bibliothek/ausleihen")
    Call<List<Ausleihe>> getAllAusleihen();

    /** POST http://192.168.1.193:8080/bibliothek/ausleihe (neu) */
    @POST("bibliothek/ausleihe")
    Call<Ausleihe> addAusleihe(@Body Ausleihe ausleihe);

    /** POST http://192.168.1.193:8080/bibliothek/ausleihe (mit ID im Body = Update) */
    @POST("bibliothek/ausleihe")
    Call<Ausleihe> updateAusleihe(@Body Ausleihe ausleihe);

    /** DELETE http://192.168.1.193:8080/bibliothek/ausleihe/{id} */
    @DELETE("bibliothek/ausleihe/{id}")
    Call<Void> deleteAusleihe(@Path("id") long id);
}