package com.pax.bibliothek;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pax.bibliothek.api.BibliothekProxy;
import com.pax.bibliothek.api.RetrofitFactory;
import com.pax.bibliothek.model.Ausleihe;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Datei: AusleiheDetailActivity.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Activity zum Erstellen einer neuen Ausleihe und zum Anzeigen sowie Verlängern
 * einer bestehenden Ausleihe.
 * Ohne Intent-Extra wird eine neue Ausleihe angelegt (POST),
 * mit Extra "ausleihe" wird die vorhandene Ausleihe angezeigt und kann verlängert werden (PUT).
 *
 * @author Abdullah M. H.
 * @version 1.0
 */
public class AusleiheDetailActivity extends AppCompatActivity {

    // Standard-Leihdauer in Tagen laut Aufgabenstellung
    private static final int LEIHDAUER = 14;

    private EditText edtKundeId;
    private EditText edtMediumId;
    private TextView tvAusleihdatum;
    private TextView tvRueckgabedatum;
    private Button btnSpeichern;
    private Button btnVerlaengern;
    private Button btnAbbrechen;
    private Ausleihe current;
    private BibliothekProxy proxy;

    /**
     * Initialisiert die Activity, bindet die Views und unterscheidet zwischen
     * Erfassen-Modus (neue Ausleihe) und Anzeige-Modus (bestehende Ausleihe).
     *
     * @param savedInstanceState zuvor gespeicherter Zustand der Activity oder {@code null}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausleihe_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Alle Views mit den XML-Elementen verbinden
        edtKundeId       = findViewById(R.id.edtKundeId);
        edtMediumId      = findViewById(R.id.edtMediumId);
        tvAusleihdatum   = findViewById(R.id.tvAusleihdatum);
        tvRueckgabedatum = findViewById(R.id.tvRueckgabedatum);
        btnSpeichern     = findViewById(R.id.btnSpeichern);
        btnVerlaengern   = findViewById(R.id.btnVerlaengern);
        btnAbbrechen     = findViewById(R.id.btnAbbrechen);

        proxy = RetrofitFactory.getRetrofitInstance().create(BibliothekProxy.class);

        // Modus bestimmen: Anzeige-Modus wenn AusleiheListActivity ein Ausleihe-Objekt uebergeben hat
        java.io.Serializable obj = getIntent().getSerializableExtra("ausleihe");
        if (obj instanceof Ausleihe) {
            current = (Ausleihe) obj;
            updateFieldsFromObject(current);
            // Im Anzeige-Modus: Eingabefelder sperren, nur Verlaengern erlauben
            edtKundeId.setEnabled(false);
            edtMediumId.setEnabled(false);
            btnSpeichern.setEnabled(false);
            btnVerlaengern.setEnabled(true);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Ausleihe anzeigen");
            }
        } else {
            // Erfassen-Modus: Vorschau für Leihdatum (heute) und Rückgabedatum (heute + LEIHDAUER)
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
            tvAusleihdatum.setText(df.format(new Date()));
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, LEIHDAUER);
            tvRueckgabedatum.setText(df.format(cal.getTime()));
            btnSpeichern.setEnabled(true);
            btnVerlaengern.setEnabled(false);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Ausleihe erfassen");
            }
        }

        btnSpeichern.setOnClickListener(v -> saveAusleihe());
        btnVerlaengern.setOnClickListener(v -> extendAusleihe());
        btnAbbrechen.setOnClickListener(v -> finish());
    }

    /**
     * Befüllt alle Felder aus der übergebenen Ausleihe.
     *
     * @param a Die Ausleihe, deren Werte angezeigt werden sollen
     */
    public synchronized void updateFieldsFromObject(Ausleihe a) {
        edtKundeId.setText(String.valueOf(a.getKunde().getId()));
        edtMediumId.setText(String.valueOf(a.getMedium().getId()));
        tvAusleihdatum.setText(a.getLeihdatumLokalisiert());
        tvRueckgabedatum.setText(a.getFaelligkeitsdatumLokalisiert());
    }

    /**
     * Leert alle Eingabe- und Anzeigefelder.
     */
    public synchronized void cleanAllFields() {
        edtKundeId.setText("");
        edtMediumId.setText("");
        tvAusleihdatum.setText("");
        tvRueckgabedatum.setText("");
    }

    /**
     * Legt eine neue Ausleihe an (POST). Validiert Kunden-ID und Inventarnummer
     * und setzt Leihdatum auf heute mit LEIHDAUER Tagen Leihdauer.
     * Mappt auf: POST /bibliothek/ausleihen
     */
    private void saveAusleihe() {
        try {
            String kidStr = edtKundeId.getText().toString().trim();
            String midStr = edtMediumId.getText().toString().trim();
            if (kidStr.isEmpty() || midStr.isEmpty()) {
                Toast.makeText(this, "Kunden-ID und Inventarnummer sind Pflicht",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            long kid = Long.parseLong(kidStr);
            long mid = Long.parseLong(midStr);
            Ausleihe a = new Ausleihe(kid, mid);
            // a.setLeihdatum(new Date());  // Backend setzt das Datum selbst
            // a.setLeihdauer((short) LEIHDAUER);  // Backend setzt die Leihdauer selbst  // Standard-Leihdauer in Tagen laut Aufgabenstellung
            proxy.addAusleihe(a).enqueue(new Callback<Ausleihe>() {
                @Override
                public void onResponse(Call<Ausleihe> call, Response<Ausleihe> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AusleiheDetailActivity.this, "Gespeichert",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        showError("Medium evtl. bereits ausgeliehen (HTTP " + response.code() + ")");
                    }
                }

                @Override
                public void onFailure(Call<Ausleihe> call, Throwable t) {
                    showError(t.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ungültiger Zahl in Kunden-ID oder Inventarnummer",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Verlängert die aktuelle Ausleihe (PUT) durch Setzen des Leihdatums auf heute
     * und Erhöhen der Leihdauer um 14 Tage, maximal jedoch auf 28 Tage.
     * Mappt auf: PUT /bibliothek/ausleihen/{id}
     */
    private void extendAusleihe() {
        int aktuelleLeihdauer = current.getLeihdauer() != null ? current.getLeihdauer() : 14;

        // Maximale Leihdauer 28 Tage pruefen
        if (aktuelleLeihdauer >= 28) {
            showError("Maximale Leihdauer von 28 Tagen erreicht.");
            return;
        }

        // Neue Leihdauer: aktuell + 14, max 28
        short neueLeihdauer = (short) Math.min(aktuelleLeihdauer + 14, 28);

        // Update-Objekt mit allen nötigen Feldern
        Ausleihe update = new Ausleihe(
                current.getKunde() != null ? current.getKunde().getId() : 0L,
                current.getMedium() != null ? current.getMedium().getId() : 0L);
        update.setId(current.getId());
        update.setLeihdatum(new Date());
        update.setLeihdauer(neueLeihdauer);

        proxy.updateAusleihe(update).enqueue(new Callback<Ausleihe>() {
            @Override
            public void onResponse(Call<Ausleihe> call, Response<Ausleihe> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AusleiheDetailActivity.this, "Verlaengert",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showError("Fehler beim Verlaengern (HTTP " + response.code() + ")");
                }
            }
            @Override
            public void onFailure(Call<Ausleihe> call, Throwable t) {
                showError(t.getMessage());
            }
        });
    }

    /**
     * Zeigt einen Fehlerdialog mit der übergebenen Meldung an.
     *
     * @param msg Fehlermeldung
     */
    private void showError(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Fehler")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}