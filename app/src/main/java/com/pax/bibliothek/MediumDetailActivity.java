package com.pax.bibliothek;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pax.bibliothek.api.BibliothekProxy;
import com.pax.bibliothek.api.RetrofitFactory;
import com.pax.bibliothek.model.Medium;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Datei: MediumDetailActivity.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 *
 * Formular zum Erfassen eines neuen Bibliotheksmediums (POST) oder
 * zum Bearbeiten eines vorhandenen Mediums (PUT).
 *
 * @author Abdullah Muhamedu
 * @version 1.0
 */

/**
 * Activity zum Erfassen und Bearbeiten eines Bibliotheksmediums.
 * Ohne Intent-Extra wird ein neues Medium angelegt (POST),
 * mit Extra "medium" wird das vorhandene Medium bearbeitet (PUT).
 *
 * @author Abdullah Muhamedu
 */
public class MediumDetailActivity extends AppCompatActivity {

    private EditText edtTitel;
    private EditText edtAutor;
    private EditText edtGenre;
    private EditText edtEan;
    private EditText edtStandort;
    private EditText edtFsk;
    private Button btnSpeichern;
    private Button btnAbbrechen;
    private Medium current;
    private BibliothekProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Alle Eingabefelder und Buttons mit den XML-Views verbinden
        edtTitel    = findViewById(R.id.edtTitel);
        edtAutor    = findViewById(R.id.edtAutor);
        edtGenre    = findViewById(R.id.edtGenre);
        edtEan      = findViewById(R.id.edtEan);
        edtStandort = findViewById(R.id.edtStandort);
        edtFsk      = findViewById(R.id.edtFsk);
        btnSpeichern = findViewById(R.id.btnSpeichern);
        btnAbbrechen = findViewById(R.id.btnAbbrechen);

        proxy = RetrofitFactory.getRetrofitInstance().create(BibliothekProxy.class);

        // Modus bestimmen: Edit-Modus wenn MediumListActivity ein Medium-Objekt uebergeben hat
        java.io.Serializable obj = getIntent().getSerializableExtra("medium");
        if (obj instanceof Medium) {
            current = (Medium) obj;
            updateFieldsFromObject(current);
            // Titel und Autor sind unveraenderliche Pflichtfelder nach der Anlage
            edtTitel.setEnabled(false);
            edtTitel.setBackgroundColor(0xFFEAEDF0);
            edtAutor.setEnabled(false);
            edtAutor.setBackgroundColor(0xFFEAEDF0);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Medium bearbeiten");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Medium erfassen");
            }
        }

        btnSpeichern.setOnClickListener(v -> saveMedium());
        btnAbbrechen.setOnClickListener(v -> finish());
    }

    /**
     * Befuellt alle Eingabefelder aus dem uebergebenen Medium-Objekt.
     *
     * @param m Das Medium, dessen Werte angezeigt werden sollen
     */
    public synchronized void updateFieldsFromObject(Medium m) {
        edtTitel.setText(m.getTitel() != null ? m.getTitel() : "");
        edtAutor.setText(m.getAutor() != null ? m.getAutor() : "");
        edtGenre.setText(m.getGenre() != null ? m.getGenre() : "");
        edtEan.setText(m.getEan() != null ? String.valueOf(m.getEan()) : "");
        edtStandort.setText(m.getStandort() != null ? m.getStandort() : "");
        edtFsk.setText(m.getFsk() != null ? String.valueOf(m.getFsk()) : "");
    }

    /**
     * Leert alle Eingabefelder.
     */
    public synchronized void cleanAllFields() {
        edtTitel.setText("");
        edtAutor.setText("");
        edtGenre.setText("");
        edtEan.setText("");
        edtStandort.setText("");
        edtFsk.setText("");
    }

    /**
     * Liest alle Eingabefelder aus, validiert die Pflichtfelder und
     * erstellt daraus ein Medium-Objekt fuer den API-Aufruf.
     * Wirft {@link NumberFormatException} bei ungueltigen numerischen Eingaben.
     *
     * @return Befuelltes Medium-Objekt oder {@code null} bei Validierungsfehler
     */
    private synchronized Medium createFromFields() {
        String titel = edtTitel.getText().toString().trim();
        String autor = edtAutor.getText().toString().trim();
        if (titel.isEmpty() || autor.isEmpty()) {
            Toast.makeText(this, "Titel und Autor sind Pflicht", Toast.LENGTH_SHORT).show();
            return null;
        }
        // Im Edit-Modus das bestehende Objekt weiterverwenden, damit die ID erhalten bleibt
        Medium m = (current != null) ? current : new Medium(titel, autor);
        m.setGenre(emptyToNull(edtGenre));
        m.setStandort(emptyToNull(edtStandort));
        m.setEan(parseLongOrNull(edtEan));
        m.setFsk(parseShortOrNull(edtFsk));
        return m;
    }

    /**
     * Speichert das Medium via POST (Erfassen) oder PUT (Bearbeiten).
     * Zeigt einen Toast bei Validierungsfehlern oder ungueltigem Zahlenformat.
     * Mappt auf POST /bibliothek/medien (neu) oder PUT /bibliothek/medien/{id} (bearbeiten).
     */
    private void saveMedium() {
        try {
            Medium m = createFromFields();
            if (m == null) return;
            // Neues Medium: POST; vorhandenes Medium: PUT mit ID
            Call<Medium> call = (current == null)
                    ? proxy.addMedium(m)
                    : proxy.updateMedium(m);
            call.enqueue(new Callback<Medium>() {
                @Override
                public void onResponse(Call<Medium> call, Response<Medium> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MediumDetailActivity.this, "Gespeichert", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        showError("HTTP " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Medium> call, Throwable t) {
                    showError(t.getMessage());
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ungültige Zahl in EAN oder Altersfreigabe", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Zeigt einen Fehlerdialog mit der uebergebenen Meldung an.
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

    /**
     * Gibt den getrimmten Text des EditText zurueck oder {@code null} wenn leer.
     *
     * @param e Das EditText-Feld
     * @return Getrimmter Text oder {@code null}
     */
    private static String emptyToNull(EditText e) {
        String s = e.getText().toString().trim();
        return s.isEmpty() ? null : s;
    }

    /**
     * Parst den Inhalt des EditText als {@link Long} oder gibt {@code null} zurueck wenn leer.
     * Wirft {@link NumberFormatException} bei ungueltiger Eingabe.
     *
     * @param e Das EditText-Feld
     * @return Geparster Long-Wert oder {@code null}
     */
    private static Long parseLongOrNull(EditText e) {
        String s = e.getText().toString().trim();
        return s.isEmpty() ? null : Long.parseLong(s);
    }

    /**
     * Parst den Inhalt des EditText als {@link Short} oder gibt {@code null} zurueck wenn leer.
     * Wirft {@link NumberFormatException} bei ungueltiger Eingabe.
     *
     * @param e Das EditText-Feld
     * @return Geparster Short-Wert oder {@code null}
     */
    private static Short parseShortOrNull(EditText e) {
        String s = e.getText().toString().trim();
        return s.isEmpty() ? null : Short.parseShort(s);
    }
}
