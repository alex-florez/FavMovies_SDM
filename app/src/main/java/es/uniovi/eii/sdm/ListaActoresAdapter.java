package es.uniovi.eii.sdm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import es.uniovi.eii.sdm.modelo.Actor;

public class ListaActoresAdapter extends RecyclerView.Adapter<ListaActoresAdapter.ActorViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Actor item);
    }

    private List<Actor> actores;
    private final OnItemClickListener listener;

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_actor, parent, false);
        return new ActorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        Actor actor = actores.get(position);
        holder.bindActor(actor, listener);
    }

    @Override
    public int getItemCount() {
        return actores.size();
    }

    public ListaActoresAdapter(List<Actor> actores, OnItemClickListener listener){
        this.actores = actores;
        this.listener = listener;
    }


    public static class ActorViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagen;
        private TextView nombre;
        private TextView personaje;

        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = (ImageView)itemView.findViewById(R.id.imagen_actor);
            nombre = (TextView)itemView.findViewById(R.id.nombre_actor);
            personaje = (TextView)itemView.findViewById(R.id.nombre_personaje);
        }

        public void bindActor(final Actor actor, final OnItemClickListener listener){

            nombre.setText(actor.getNombre());
            personaje.setText(actor.getNombre_personaje());
            Picasso.get()
                   .load(actor.getImagen()).into(imagen);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(actor);
                }
            });
        }
    }
}
