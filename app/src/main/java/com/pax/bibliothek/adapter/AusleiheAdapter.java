package com.pax.bibliothek.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.bibliothek.R;
import com.pax.bibliothek.model.Ausleihe;

import java.util.ArrayList;
import java.util.List;

/**
 * Datei: AusleiheAdapter.java
 * Projekt: Bibliothek-App (UEK Modul 335)
 * Adapter fuer die RecyclerView der Ausleihen.
 * Zeigt Kunde, Medium und Faelligkeitsdatum je Eintrag.
 *
 * @author Abdullah M. H.
 * @version 1.0
 */
public class AusleiheAdapter extends RecyclerView.Adapter<AusleiheAdapter.ViewHolder> {

    private final List<Ausleihe> data = new ArrayList<>();
    private final AusleiheListener listener;

    public AusleiheAdapter(AusleiheListener listener) {
        this.listener = listener;
    }

    /**
     * Ersetzt die Datenliste und aktualisiert die Anzeige.
     * synchronized, weil der Aufruf aus dem Retrofit-Callback kommt.
     *
     * @param list neue Liste aller Ausleihen
     */
    public synchronized void setData(List<Ausleihe> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Erzeugt einen neuen ViewHolder fuer ein Ausleihe-Item, indem das
     * Layout item_ausleihe aufgeblasen wird.
     *
     * @param parent   das ViewGroup, in das die neue View eingehaengt wird
     * @param viewType der View-Typ (hier nicht verwendet)
     * @return ein neuer ViewHolder fuer ein Listenelement
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ausleihe, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Bindet die Daten einer Ausleihe an die Views des ViewHolders und
     * setzt die Click-Listener fuer Detailansicht und Loeschen.
     *
     * @param holder   der zu befuellende ViewHolder
     * @param position Position des Elements in der Liste
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ausleihe a = data.get(position);

        // Format: "Kunde 42  -  Medium 17"
        String kundeId  = (a.getKunde()  != null) ? String.valueOf(a.getKunde().getId())  : "?";
        String mediumId = (a.getMedium() != null) ? String.valueOf(a.getMedium().getId()) : "?";
        holder.tvKundeMedium.setText((a.getKunde() != null ? a.getKunde().getVorname() + " " + a.getKunde().getFamilienname() : "?") + "  -  " + (a.getMedium() != null ? a.getMedium().getTitel() : "?"));

        // Faelligkeit aus DTO-Methode (berechnet Leihdatum + Leihdauer)
        holder.tvFaelligkeit.setText("Fällig: " + a.getFaelligkeitsdatumLokalisiert());

        // Klick auf das Item oeffnet die Detailansicht
        holder.itemView.setOnClickListener(v -> listener.onItemClick(a));

        // Klick auf Loeschen-Button entspricht der Rueckgabe des Mediums
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(a));
    }

    /**
     * Liefert die Anzahl der Ausleihen in der Liste.
     *
     * @return Anzahl der Elemente
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder fuer ein einzelnes Ausleihe-Item.
     * Haelt Referenzen auf die TextViews und den Loeschen-Button.
     *
     * @author Abdullah M. H.
     * @version 1.0
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvKundeMedium;
        final TextView tvFaelligkeit;
        final ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKundeMedium = itemView.findViewById(R.id.tvKundeMedium);
            tvFaelligkeit = itemView.findViewById(R.id.tvFaelligkeit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}