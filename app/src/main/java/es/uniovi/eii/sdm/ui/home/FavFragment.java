package es.uniovi.eii.sdm.ui.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.eii.sdm.ListaPeliculasAdapter;
import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.activities.ShowMovie;
import es.uniovi.eii.sdm.datos.PeliculasDataSource;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class FavFragment extends Fragment {

    public static final String PELICULA_SELECCIONADA = "peliSeleccionada";
    public static final String PELICULA_CREADA = "peliCreada";

    View root;
    RecyclerView recyclerView;

    List<Pelicula> listaPeli;
    Pelicula pelicula;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_fav, container, false);

        recyclerView = (RecyclerView)root.findViewById(R.id.recycleViewFav);
        recyclerView.setHasFixedSize(true);
        cargarBD();
        return root;
    }

    private void cargarBD(){
        PeliculasDataSource peliculasDataSource = new PeliculasDataSource(getContext());
        peliculasDataSource.open();
        listaPeli = peliculasDataSource.getAll();
        peliculasDataSource.close();
        mostrarListaPeliculas();
    }

    private void mostrarListaPeliculas() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Creamos el adapter
        ListaPeliculasAdapter lpAdapter = new ListaPeliculasAdapter(listaPeli,
                new ListaPeliculasAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Pelicula item) {
                        clickOnItem(item);
                    }
                });

        recyclerView.setAdapter(lpAdapter);
    }

    /**
     * Método que será invocado cuando se haga click sobre un elemento
     * del recyclerView. Crea un intent para iniciar el MainActivity pasándole
     * la película.
     * @param pelicula
     */
    private void clickOnItem(Pelicula pelicula){
        // Paso el modo de apertura
        Intent intent = new Intent(this.getContext(), ShowMovie.class);
        intent.putExtra(PELICULA_SELECCIONADA, pelicula);
        // Transición de barrido a la activity para ver la pelicula.
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this.getActivity()).toBundle());
    }

}