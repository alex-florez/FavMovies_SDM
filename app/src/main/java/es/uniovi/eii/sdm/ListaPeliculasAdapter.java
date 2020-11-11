package es.uniovi.eii.sdm;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.uniovi.eii.sdm.modelo.Pelicula;

public class ListaPeliculasAdapter extends RecyclerView.Adapter<ListaPeliculasAdapter.PeliculaViewHolder> {

    // Interfaz para manejar el evento click sobre un elemento
    public interface OnItemClickListener {
        void onItemClick(Pelicula item);
    }

    private List<Pelicula> listaPeliculas;
    private final OnItemClickListener listener;

    public ListaPeliculasAdapter(List<Pelicula> listaPeliculas, OnItemClickListener listener){
        this.listaPeliculas = listaPeliculas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos la vista con el layout para un elemento
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_pelicula, parent, false);

        return new PeliculaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        Pelicula pelicula = listaPeliculas.get(position);
        holder.bindMovie(pelicula, listener); // Asociación entre item y vista del item
    }

    @Override
    public int getItemCount() {
        return listaPeliculas.size();
    }


    // Clase interna para la vista de cada item
    // ****************************************
    public static class PeliculaViewHolder extends RecyclerView.ViewHolder {
        private TextView titulo;
        private TextView fecha;
        private ImageView imagen;

        public PeliculaViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = (TextView)itemView.findViewById(R.id.txtTitulo);
            fecha = (TextView)itemView.findViewById(R.id.txtFecha);
            imagen = (ImageView)itemView.findViewById(R.id.imgPelicula);
        }

        // Asignar valores a los componentes
        public void bindMovie(final Pelicula pelicula, final OnItemClickListener listener){
            titulo.setText(pelicula.getTitulo());
            fecha.setText(pelicula.getFecha());
            // Cargar imagen
            Picasso.get()
                    .load(pelicula.getUrlCaratula()).into(imagen);
            Log.d("PELICULA", pelicula.getUrlFondo());

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pelicula);
                }
            });
        }
    }
}
