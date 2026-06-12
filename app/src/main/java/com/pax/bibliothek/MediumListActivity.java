package com.pax.bibliothek;

/**
 * Android Standard-Komponenten (UI & App-Grundlagen)
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
/**
 * AndroidX (moderne UI / Layout / Navigation)
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/**
 * eigeni Appzügs
 */
import com.pax.bibliothek.adapter.MediumAdapter;
import com.pax.bibliothek.adapter.MediumListener;
import com.pax.bibliothek.api.BibliothekProxy;
import com.pax.bibliothek.api.RetrofitFactory;
import com.pax.bibliothek.model.Medium;
/**
 * standart libary
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Retrofit (Libary für API aufrufe)
 */
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Datei: MediumListActivity.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Activity zur Anzeige und Verwaltung der Medienliste.
 * Zeigt alle Bibliotheksmedien in einer scrollbaren Liste an und unterstützt
 * Sortierung nach Titel oder Autor (auf- und absteigend), das Anlegen neuer
 * Medien sowie das Bearbeiten und Löschen vorhandener Einträge.
 * Implementiert {@link MediumListener}, um Klick- und Löschereignisse aus dem Adapter zu empfangen.
 *
 * @author Abdullah M. H.
 * @version 1.0
 */
public class MediumListActivity extends AppCompatActivity implements MediumListener {

    private MediumAdapter adapter;
    private Spinner spnSortField;
    private Spinner spnSortDirection;
    private List<Medium> medienListe = new ArrayList<>();
    private BibliothekProxy proxy;

    /**
     * Initialisiert die Activity, bindet Toolbar, RecyclerView und Sortier-Spinner
     * und löst das erste Laden der Medienliste aus.
     *
     * @param savedInstanceState zuvor gespeicherter Zustand der Activity oder {@code null}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_list);

        // Toolbar als ActionBar registrieren (ermoeglicht Menue und Titel)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrofit-Proxy fuer alle API-Aufrufe in dieser Activity
        proxy = RetrofitFactory.getRetrofitInstance().create(BibliothekProxy.class);

        // RecyclerView mit LinearLayout und eigenem Adapter verbinden
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MediumAdapter(this);
        recyclerView.setAdapter(adapter);

        spnSortField = findViewById(R.id.spnSortField);
        spnSortDirection = findViewById(R.id.spnSortDirection);

        // Sortierfeld-Spinner (Titel / Autor) aus String-Array-Ressource befuellen
        ArrayAdapter<CharSequence> fieldAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_fields, android.R.layout.simple_spinner_item);
        fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSortField.setAdapter(fieldAdapter);

        // Sortierrichtung-Spinner (Aufsteigend / Absteigend) aus String-Array-Ressource befuellen
        ArrayAdapter<CharSequence> dirAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_directions, android.R.layout.simple_spinner_item);
        dirAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSortDirection.setAdapter(dirAdapter);

        // Beide Spinner nutzen denselben Listener; jede Aenderung loest eine Neusortierung aus
        AdapterView.OnItemSelectedListener sortListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortAndShow();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        spnSortField.setOnItemSelectedListener(sortListener);
        spnSortDirection.setOnItemSelectedListener(sortListener);

        // Benutzer tippt "Neu": oeffnet Detail-Activity im Erfassen-Modus (ohne Extra)
        Button btnNeu = findViewById(R.id.btnNeu);
        btnNeu.setOnClickListener(v ->
                startActivity(new Intent(this, MediumDetailActivity.class)));

        loadMedien();
    }

    /**
     * Wird beim Wiederanzeigen der Activity aufgerufen und lädt die Medienliste
     * neu, damit Änderungen aus der Detail-Activity sichtbar werden.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Liste nach Rueckkehr aus Detail-Activity aktualisieren
        loadMedien();
    }

    /**
     * Lädt alle Medien vom Backend und aktualisiert die Liste.
     * Mappt auf: GET /bibliothek/medien
     * Bei Erfolg wird sortAndShow() aufgerufen; bei Netzwerkfehler erscheint ein Dialog.
     */
    private void loadMedien() {
        proxy.getAllMedien().enqueue(new Callback<List<Medium>>() {
            @Override
            public void onResponse(Call<List<Medium>> call, Response<List<Medium>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    medienListe = response.body();
                    sortAndShow();
                }
            }

            @Override
            public void onFailure(Call<List<Medium>> call, Throwable t) {
                new AlertDialog.Builder(MediumListActivity.this)
                        .setTitle("Fehler")
                        .setMessage(t.getMessage())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    /**
     * Sortiert die gespeicherte Medienliste nach den gewählten Kriterien
     * und übergibt sie dem Adapter.
     * Spinner-Position 0 = Titel, 1 = Autor; Richtung 0 = aufsteigend, 1 = absteigend.
     */
    private synchronized void sortAndShow() {
        List<Medium> sorted = new ArrayList<>(medienListe);
        final int field = spnSortField.getSelectedItemPosition();
        final int dir = spnSortDirection.getSelectedItemPosition();

        // Comparator: null-Werte werden als leerer String behandelt, um NullPointerException zu vermeiden
        Collections.sort(sorted, (a, b) -> {
            String valA = field == 0 ? a.getTitel() : a.getAutor();
            String valB = field == 0 ? b.getTitel() : b.getAutor();
            if (valA == null) valA = "";
            if (valB == null) valB = "";
            int cmp = valA.compareToIgnoreCase(valB);
            // dir == 1 bedeutet absteigend: Vergleichsergebnis umkehren
            return dir == 1 ? -cmp : cmp;
        });
        adapter.setData(sorted);
    }

    /**
     * {@inheritDoc}
     * Öffnet die MediumDetailActivity im Bearbeiten-Modus mit dem gewählten Medium als Extra.
     */
    @Override
    public void onItemClick(Medium medium) {
        Intent intent = new Intent(this, MediumDetailActivity.class);
        intent.putExtra("medium", medium);
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     * Zeigt einen Bestätigungsdialog und löscht das Medium via DELETE-Endpoint.
     */
    @Override
    public void onDeleteClick(Medium medium) {
        new AlertDialog.Builder(this)
                .setTitle("Medium löschen")
                .setMessage("Medium \"" + medium.getTitel() + "\" sicher löschen?")
                .setPositiveButton("Ja", (dialog, which) ->
                        // Benutzer hat bestaetigt: DELETE /bibliothek/medien/{id}
                        proxy.deleteMedium(medium.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                loadMedien();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                new AlertDialog.Builder(MediumListActivity.this)
                                        .setTitle("Fehler")
                                        .setMessage(t.getMessage())
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        }))
                .setNegativeButton("Nein", null)
                .show();
    }

    /**
     * Bläst das Options-Menü der ActionBar auf.
     *
     * @param menu das zu befüllende Menü
     * @return true, damit das Menü angezeigt wird
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Verarbeitet Klicks auf Navigationseinträge im ActionBar-Menü.
     * menu_medien: bleibt auf dieser Activity; menu_ausleihen: wechselt zu AusleiheListActivity.
     *
     * @param item Das gewählte Menüelement
     * @return true wenn verarbeitet, sonst super-Implementierung
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_medien) {
            return true;
        } else if (id == R.id.menu_ausleihen) {
            startActivity(new Intent(this, AusleiheListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}