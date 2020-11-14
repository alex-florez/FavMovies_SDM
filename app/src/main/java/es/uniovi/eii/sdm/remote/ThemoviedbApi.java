package es.uniovi.eii.sdm.remote;

import es.uniovi.eii.sdm.datos.server.actorlist.RepartoResult;
import es.uniovi.eii.sdm.datos.server.movielist.MovieListResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ThemoviedbApi {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";



    // Petición GET para obtener el reparto de una película
    @GET("movie/{movieId}/credits")
    Call<RepartoResult> getReparto(
            @Path("movieId") int movieId,
            @Query("api_key") String apiKey);

    // Petición de lista de películas populares
    //  https://api.themoviedb.org/3/movie/popular?api_key=19fe58e2500350455b56639cc24da48f&language=es-Es&page=1
    @GET("movie/{lista}")
    Call<MovieListResult> getListMovies(
            @Path("lista") String lista,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );




}
