package es.uniovi.eii.sdm.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.uniovi.eii.sdm.ListaActoresAdapter;
import es.uniovi.eii.sdm.ListaPeliculasAdapter;
import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.datos.ActorsDataSource;
import es.uniovi.eii.sdm.modelo.Actor;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class ActoresFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Actor> actores;

    private RecyclerView recyclerView;
    private ListaActoresAdapter listaActoresAdapter;

    public ActoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_actores, container, false);

        // Crear aqui el recyclerView
        recyclerView = (RecyclerView)root.findViewById(R.id.reciclerViewActores);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle args = getArguments();
        int peliculaId = args.getInt("id_pelicula");

        ActorsDataSource actorsDataSource = new ActorsDataSource(root.getContext());
        actorsDataSource.open();
        actores = actorsDataSource.actoresParticipantes(peliculaId);
        actorsDataSource.close();
        // Adapter
        listaActoresAdapter = new ListaActoresAdapter(actores, new ListaActoresAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Actor item) {

            }
        });

        recyclerView.setAdapter(listaActoresAdapter);

        // Referencias a componentes
        return root;
    }
}