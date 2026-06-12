package com.pax.bibliothek.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.bibliothek.R;
import com.pax.bibliothek.model.Medium;

import java.util.ArrayList;
import java.util.List;

/**
 * Datei: MediumAdapter.java
 * Projekt: Bibliothek-App (üK Modul 335)
 *
 * RecyclerView-Adapter fuer die Medienliste. Bindet jedes Medium-Objekt
 * an ein Listenelement-Layout und leitet Benutzerinteraktionen weiter.
 *
 */

/**
 * RecyclerView-Adapter fuer die Medien-Liste.
 * Zeigt Inventarnummer, Titel und Autor jedes Mediums an
 * und leitet Klick- und Loeschereignisse an den {@link MediumListener} weiter.
 * * @author Abdullah Muhamedu
 *  * @version 1.0
 **/
public class MediumAdapter extends RecyclerView.Adapter<MediumAdapter.ViewHolder> {

    private List<Medium> data = new ArrayList<>();
    private final MediumListener listener;

    public MediumAdapter(MediumListener listener) {
        this.listener = listener;
    }

    /**
     * Ersetzt die angezeigte Liste und aktualisiert die Ansicht.
     *
     * @param list Neue Medienliste
     */
    public synchronized void setData(List<Medium> list) {
        data = list;
        notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medium, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@inheritDoc}
     * Zeigt Inventarnummer und Titel in der Form "ID · Titel" an.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medium medium = data.get(position);
        // Format: "Inventarnummer · Titel" fuer schnelle Identifikation in der Liste
        holder.tvInventarTitel.setText(medium.getId() + " · " + medium.getTitel());
        holder.tvAutor.setText(medium.getAutor());
        // Klick auf gesamte Zeile oeffnet den Bearbeiten-Modus
        holder.itemView.setOnClickListener(v -> listener.onItemClick(medium));
        // Klick auf Loeschen-Button loest Bestaetigungsdialog in der Activity aus
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(medium));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder fuer ein Medium-Listenelement.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvInventarTitel;
        final TextView tvAutor;
        final ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvInventarTitel = itemView.findViewById(R.id.tvInventarTitel);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
