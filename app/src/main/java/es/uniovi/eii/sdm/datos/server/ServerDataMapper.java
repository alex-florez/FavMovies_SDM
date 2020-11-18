package es.uniovi.eii.sdm.datos.server;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.datos.server.credits.Cast;
import es.uniovi.eii.sdm.datos.server.movielist.MovieData;
import es.uniovi.eii.sdm.modelo.Actor;
import es.uniovi.eii.sdm.modelo.Categoria;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class ServerDataMapper {
    private static final String BASE_URL_IMG= "https://image.tmdb.org/t/p/";
    private static final String IMG_W342= "w342";
    private static final String IMG_ORIGINAL= "original";


    /**
     * Convierte datos de la API de cada película a datos del dominio
     * Pelicula     <--     MovieData
     *
     * int id;              <-- Integer id
     * String titulo;       <-- title
     * String argumento;    <-- overview
     * Categoria categoria; No lo rellenamos ya que sólo aparece id
     * String duracion;     No tenemos este dato
     * String fecha;        <-- releaseDate (yyyy-mm-dd)
     *
     * String urlCaratula;  <-- posterPath, hay que completar la url
     * String urlFondo;     <-- backdropPath
     * String urlTrailer;   No tenemos este dato
     *
     * @param movieData
     * @return
     */
    public static List<Pelicula> convertMovieListToDomain(List<MovieData> movieData) {
        ArrayList<Pelicula> lpeliculas= new ArrayList<Pelicula>();

        for (MovieData peliApi: movieData) {
            String urlCaratula;
            String urlFondo;

            if (peliApi.getPosterPath()==null) {
                urlCaratula= "";
            } else {
                urlCaratula= BASE_URL_IMG + IMG_W342 + peliApi.getPosterPath();
            }
            if (peliApi.getBackdropPath()==null) {
                urlFondo= "";
            } else {
                urlFondo = BASE_URL_IMG + IMG_ORIGINAL + peliApi.getBackdropPath();
            }

            lpeliculas.add(new Pelicula(peliApi.getId(),
                    peliApi.getTitle(),
                    peliApi.getOverview(),
                    new Categoria("",""),
                    "",
                    peliApi.getReleaseDate(),
                    urlCaratula,
                    urlFondo,
                    ""
            ));
        }

        return lpeliculas;
    }

    public static List<Actor> convertCastListToDomain(List<Cast> castList) {
        ArrayList<Actor> lactores = new ArrayList<Actor>();

        for (Cast cast : castList) {
            String name = cast.getName();
            String character = cast.getCharacter();
            String imagen = "";

            if(cast.getProfilePath() != null){
                imagen = BASE_URL_IMG + IMG_ORIGINAL + cast.getProfilePath();
            }
            lactores.add(new Actor(
                    cast.getId(),
                    name,
                    character,
                    imagen,
                    ""
            ));
        }

        return lactores;
    }
}
