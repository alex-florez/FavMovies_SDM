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
import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.activities.MainRecycler;
import es.uniovi.eii.sdm.datos.server.ServerDataMapper;
import es.uniovi.eii.sdm.datos.server.credits.Cast;
import es.uniovi.eii.sdm.datos.server.credits.RepartoResult;
import es.uniovi.eii.sdm.modelo.Actor;
import es.uniovi.eii.sdm.remote.ApiUtils;
import es.uniovi.eii.sdm.remote.ThemoviedbApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActoresFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Actor> actores;
    private int movieId;

    private RecyclerView recyclerView;
    private ListaActoresAdapter listaActoresAdapter;

    public ActoresFragment() {
        // Required empty public constructor
    }

    private void loadReparto(){
        ThemoviedbApi themoviedbApi = ApiUtils.createThemoviedbApi();
        Call<RepartoResult> call = themoviedbApi.getReparto(this.movieId, MainRecycler.API_KEY);

        call.enqueue(new Callback<RepartoResult> () {
            @Override
            public void onResponse(Call<RepartoResult> call, Response<RepartoResult> response) {

                switch (response.code()){
                    case 200:
                        RepartoResult data = response.body();
                        List<Cast> casts = data.getCast();
                        actores = ServerDataMapper.convertCastListToDomain(casts);
                        listaActoresAdapter = new ListaActoresAdapter(actores, new ListaActoresAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Actor item) {

                            }
                        });

                        recyclerView.setAdapter(listaActoresAdapter);
                        break;
                    default:
                        call.cancel();
                        break;
                }
            }

            @Override
            public void onFailure(Call<RepartoResult> call, Throwable t) {
                Log.e("CargarReparto", "Error al cargar el reparto: " + t.getMessage());
            }
        });
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
        movieId = args.getInt("id_pelicula");

        // Llamada a la API para obtener el reparto.
        loadReparto();
//        ActorsDataSource actorsDataSource = new ActorsDataSource(root.getContext());
//        actorsDataSource.open();
//        actores = actorsDataSource.actoresParticipantes(peliculaId);
//        actorsDataSource.close();
        return root;
    }
}