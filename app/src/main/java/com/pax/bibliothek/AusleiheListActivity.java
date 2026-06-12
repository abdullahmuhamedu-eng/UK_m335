package com.pax.bibliothek;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.bibliothek.adapter.AusleiheAdapter;
import com.pax.bibliothek.adapter.AusleiheListener;
import com.pax.bibliothek.api.BibliothekProxy;
import com.pax.bibliothek.api.RetrofitFactory;
import com.pax.bibliothek.model.Ausleihe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Datei: AusleiheListActivity.java
 * Projekt: Bibliothek-App (ÜK Modul 335)
 *
 * Zeigt alle aktiven Ausleihen in einer scrollbaren Liste an.
 * Ermoeglicht das Anlegen neuer Ausleihen und das Beenden (Zurueckgeben) bestehender.
 * Activity zur Anzeige und Verwaltung der Ausleihen-Liste.
 *  * Ermoeglicht das Anlegen neuer Ausleihen sowie das Zurueckgeben (Loeschen) vorhandener.
 *  * Implementiert {@link AusleiheListener}, um Klick- und Loeschereignisse aus dem Adapter zu empfangen.
 *  *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

public class AusleiheListActivity extends AppCompatActivity implements AusleiheListener {

    private AusleiheAdapter adapter;
    private BibliothekProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausleihe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        proxy = RetrofitFactory.getRetrofitInstance().create(BibliothekProxy.class);

        // RecyclerView mit LinearLayout und eigenem Adapter verbinden
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AusleiheAdapter(this);
        recyclerView.setAdapter(adapter);

        // Benutzer tippt "Neu": oeffnet Detail-Activity im Erfassen-Modus (ohne Extra)
        Button btnNeu = findViewById(R.id.btnNeu);
        btnNeu.setOnClickListener(v ->
                startActivity(new Intent(this, AusleiheDetailActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Liste nach Rueckkehr aus Detail-Activity aktualisieren
        loadAusleihen();
    }

    /**
     * Laedt alle Ausleihen vom Backend und aktualisiert die Liste.
     * Mappt auf: GET /bibliothek/ausleihen
     * Bei Netzwerkfehler erscheint ein Fehlerdialog.
     */
    private void loadAusleihen() {
        proxy.getAllAusleihen().enqueue(new Callback<List<Ausleihe>>() {
            @Override
            public void onResponse(Call<List<Ausleihe>> call, Response<List<Ausleihe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Ausleihe>> call, Throwable t) {
                new AlertDialog.Builder(AusleiheListActivity.this)
                        .setTitle("Fehler")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    /**
     * {@inheritDoc}
     * Öffnet die AusleiheDetailActivity im Anzeige-Modus mit der gewählten Ausleihe als Extra.
     */
    @Override
    public void onItemClick(Ausleihe ausleihe) {
        Intent intent = new Intent(this, AusleiheDetailActivity.class);
        intent.putExtra("ausleihe", ausleihe);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Zeigt einen Bestaetigungsdialog und loescht die Ausleihe via DELETE-Endpoint
     * (entspricht der Rueckgabe des Mediums).
     */
    @Override
    public void onDeleteClick(Ausleihe ausleihe) {
        new AlertDialog.Builder(this)
                .setTitle("Ausleihe beenden")
                .setMessage("Medium " + (ausleihe.getMedium() != null ? ausleihe.getMedium().getId() : "?") + " zurückerhalten?")
                .setPositiveButton("Ja", (dialog, which) ->
                        // Benutzer hat bestaetigt: DELETE /bibliothek/ausleihen/{id}
                        proxy.deleteAusleihe(ausleihe.getMedium() != null ? ausleihe.getMedium().getId() : 0L).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                loadAusleihen();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                new AlertDialog.Builder(AusleiheListActivity.this)
                                        .setTitle("Fehler")
                                        .setMessage(t.getMessage())
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        }))
                .setNegativeButton("Nein", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Verarbeitet Klicks auf Navigationseintraege im ActionBar-Menue.
     * menu_medien: wechselt zu MediumListActivity; menu_ausleihen: bleibt auf dieser Activity.
     *
     * @param item Das gewaehlte Menüelement
     * @return true wenn verarbeitet, sonst super-Implementierung
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_medien) {
            startActivity(new Intent(this, MediumListActivity.class));
            return true;
        } else if (id == R.id.menu_ausleihen) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
