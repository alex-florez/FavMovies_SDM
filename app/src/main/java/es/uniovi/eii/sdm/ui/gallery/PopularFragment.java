package es.uniovi.eii.sdm.ui.gallery;

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
import es.uniovi.eii.sdm.activities.MainRecycler;
import es.uniovi.eii.sdm.activities.ShowMovie;
import es.uniovi.eii.sdm.datos.PeliculasDataSource;
import es.uniovi.eii.sdm.datos.server.ServerDataMapper;
import es.uniovi.eii.sdm.datos.server.movielist.MovieData;
import es.uniovi.eii.sdm.datos.server.movielist.MovieListResult;
import es.uniovi.eii.sdm.modelo.Pelicula;
import es.uniovi.eii.sdm.remote.ApiUtils;
import es.uniovi.eii.sdm.remote.ThemoviedbApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularFragment extends Fragment {

    public static final String LANGUAGE = "es-ES";
    public static final String API_KEY = "19fe58e2500350455b56639cc24da48f";
    public static final String PELICULA_SELECCIONADA = "peliSeleccionada";

    // Vistas
    View root;
    RecyclerView recyclerView;

    // Modelo de datos
    List<Pelicula> listaPeli;
    Pelicula pelicula;

    // Api
    ThemoviedbApi themoviedbApi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_popular, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.recycleViewPopular);
        recyclerView.setHasFixedSize(true);

        themoviedbApi = ApiUtils.createThemoviedbApi();
        realizarPeticionPeliculasPopulares(themoviedbApi);

        return root;
    }


    private void realizarPeticionPeliculasPopulares(ThemoviedbApi themoviedbApi) {
        Call<MovieListResult> call = themoviedbApi.getListMovies("popular", API_KEY, LANGUAGE, 1);

        call.enqueue(new Callback<MovieListResult>(){
            @Override
            public void onResponse(Call<MovieListResult> call, Response<MovieListResult> response) {
                switch (response.code()){
                    case 200: // Respuesta válida
                        MovieListResult data = response.body();
                        List<MovieData> listaDatosPeliculas = data.getMovieData();
                        Log.d("realizarPetPopulares", "ListaDatosPeliculas: " +
                                listaDatosPeliculas);

                        listaPeli = ServerDataMapper.convertMovieListToDomain(listaDatosPeliculas);

                        // Establecer el layout manager al recycler view, para colocar los items uno debajo del otro
                        mostrarListaPeliculas();
                        break;
                    default:
                        // log para ver código de error
                        call.cancel();
                        break;
                }
            }

            @Override
            public void onFailure(Call<MovieListResult> call, Throwable t) {
                // hay un problema en la conexión
                Log.e("ErrorPetPopulares", t.getMessage());
            }
        });
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