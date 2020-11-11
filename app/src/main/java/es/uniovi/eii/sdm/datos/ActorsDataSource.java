package es.uniovi.eii.sdm.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.modelo.Actor;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class ActorsDataSource {

    /**
     * Referencia para manejar la base de datos. Este objeto lo obtenemos a partir de MyDBHelper
     * y nos proporciona metodos para hacer operaciones
     * CRUD (create, read, update and delete)
     */
    private SQLiteDatabase database;
    /**
     * Referencia al helper que se encarga de crear y actualizar la base de datos.
     */
    private MyDBHelper dbHelper;
    /**
     * Columnas de la tabla
     */
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_REPARTO, MyDBHelper.COLUMNA_NOMBRE_ACTOR,
            MyDBHelper.COLUMNA_IMAGEN_ACTOR, MyDBHelper.COLUMNA_URL_imdb};


    /*
    Constructor
     */
    public ActorsDataSource(Context context){
        dbHelper = new MyDBHelper(context, null, null, 1);
    }

    /**
     * Abre una conexion para escritura con la base de datos.
     * Esto lo hace a traves del helper con la llamada a getWritableDatabase. Si la base de
     * datos no esta creada, el helper se encargara de llamar a onCreate
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Cierra la conexion con la base de datos
     */
    public void close() {
        dbHelper.close();
    }


    public long insertActor(Actor actor){
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_REPARTO, actor.getId());
        values.put(MyDBHelper.COLUMNA_NOMBRE_ACTOR, actor.getNombre());
        values.put(MyDBHelper.COLUMNA_IMAGEN_ACTOR, actor.getImagen());
        values.put(MyDBHelper.COLUMNA_URL_imdb, actor.getUrlIMDB());

        long insertId = database.insert(MyDBHelper.TABLA_REPARTO, null, values);
        return insertId;
    }

    public List<Actor> getAll(){
        List<Actor> actors = new ArrayList<>();
        Cursor cursor = database.query(MyDBHelper.TABLA_REPARTO, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            final Actor actor = new Actor();
            actor.setId(cursor.getInt(0));
            actor.setNombre(cursor.getString(1));
            actor.setImagen("https://image.tmdb.org/t/p/original/" + cursor.getString(2));
            actor.setUrlIMDB("https://www.imdb.com/name/" + cursor.getString(3));

            actors.add(actor);
            cursor.moveToNext();
        }

        return actors;
    }

    /**
     * Devuelve una lista con todos los actores que participan en la película con el id pasado por parámetro.
     * @param id_pelicula
     * @return reparto
     */
//    public List<Actor> actoresParticipantes(int id_pelicula) {
//        List<Actor> actors = new ArrayList<>();
//
//        /**
//         * SELECT * FROM Actores INNER JOIN RepartoPeliculas ON
//         *                 RepartoPeliculas.id_actor = Actores.id
//         *                 WHERE RepartoPeliculas.id_pelicula = id_pelicula
//         */
//        String sql = "SELECT * FROM " + MyDBHelper.TABLA_REPARTO +
//                " INNER JOIN " + MyDBHelper.TABLA_PELICULAS_REPARTO + " ON " +
//                MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_ID_REPARTO +
//                " = " + MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_ID_REPARTO +
//                " WHERE " + MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_ID_PELICULAS +
//                " = \"" + id_pelicula + "\"";
//        Cursor cursor = database.rawQuery(sql, null);
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            final Actor actor = new Actor();
//            actor.setId(cursor.getInt(0));
//            actor.setNombre(cursor.getString(1));
//            actor.setImagen("https://image.tmdb.org/t/p/original/" + cursor.getString(2));
//            actor.setUrlIMDB("https://www.imdb.com/name/" + cursor.getString(3));
//
//            actors.add(actor);
//            cursor.moveToNext();
//        }
//
//        return actors;
//    }

    /**
     * Devuelve una lista con todos los actores que participan en la película con el id pasado por parámetro.
     * @param id_pelicula
     * @return reparto
     */
    public List<Actor> actoresParticipantes(int id_pelicula) {
        List<Actor> reparto = new ArrayList<Actor>();
        // La expresión SQL correspondiente a la busqueda en un String.
        Cursor cursor = database.rawQuery("SELECT " +
                MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_NOMBRE_ACTOR + ", " +
                MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_PERSONAJE + ", " +
                MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_IMAGEN_ACTOR + ", " +
                MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_URL_imdb +
                " FROM " + MyDBHelper.TABLA_PELICULAS_REPARTO +
                " JOIN " + MyDBHelper.TABLA_REPARTO + " ON " +
                MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_ID_REPARTO +
                " = " + MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_ID_REPARTO +
                " WHERE " + MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_ID_PELICULAS + " = " + id_pelicula, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            reparto.add(new Actor(cursor.getString(0),
                    cursor.getString(1),
                    //Añadimos el encabezado de las páginas web
                    "https://image.tmdb.org/t/p/original/" + cursor.getString(2),
                    "https://www.imdb.com/name/" + cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();
        return reparto;
    }
}
