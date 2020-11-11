package es.uniovi.eii.sdm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.ListaPeliculasAdapter;
import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.datos.ActorsDataSource;
import es.uniovi.eii.sdm.datos.PeliculasDataSource;
import es.uniovi.eii.sdm.datos.RepartoPeliculaDataSource;
import es.uniovi.eii.sdm.modelo.Actor;
import es.uniovi.eii.sdm.modelo.Categoria;
import es.uniovi.eii.sdm.modelo.Pelicula;
import es.uniovi.eii.sdm.modelo.RepartoPelicula;

public class MainRecycler extends AppCompatActivity {

    public static final String PELICULA_SELECCIONADA = "peliSeleccionada";

    public static String filtroCategoria = null;

    //Objetos para las notificaciones
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;



    // Modelo de datos
    List<Pelicula> listaPeli;
    RecyclerView listaPeliView;

    // Componentes
    FloatingActionButton fab;


    private class DownloadFilesTask extends AsyncTask<Void, Integer, String> {

        private float lineasALeer;

        protected int lineasFichero(String nombreFichero) {
            InputStream file = null;
            InputStreamReader reader = null;
            BufferedReader bufferedReader = null;
            int lineas = 0;
            try {
                file = getAssets().open(nombreFichero);
                reader = new InputStreamReader(file);
                bufferedReader = new BufferedReader(reader);
                //Pase rápido mirando el total de líneas,
                // sin perder tiempo de procesamiento en nada más
                //Necesario para el progressbar
                while (bufferedReader.readLine() != null)
                    lineas++;
                bufferedReader.close();
            }catch(Exception e){};
            return lineas;
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            this.progressDialog = new ProgressDialog(MainRecycler.this);
//            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            this.progressDialog.setCancelable(false);
//            this.progressDialog.show();
//
//            //Inicializamos el lineasALeer, con un repaso a la cantidad de líneas que tienen los ficheros.
//            lineasALeer = (float) (lineasFichero("peliculas.csv"));
//            lineasALeer = (float) (lineasALeer + lineasFichero("peliculas-reparto.csv"));
//            lineasALeer = (float) (lineasALeer + lineasFichero("reparto.csv"));
//
//        }
//
//        //Este método actualiza la barra de progreso..
//        //El Integer se corresponde al parámetro indicado en el encabezado de la clase.
//        protected void onProgressUpdate(Integer... progress) {
//            progressDialog.setProgress(progress[0]);
//        }
//
//        //Método que se ejecuta tras doInBackground.
//        //El mensaje que recibe es el que devolvemos en la ejecución principal.
//        protected void onPostExecute(String message) {
//            //descartar el mensaje después de que la base de datos haya sido actualizada
//            this.progressDialog.dismiss();
//            //Avisamos que la base de datos se cargó satisfactoriamente (o hubo error, según lo que haya ocurrido)
//            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
//            //Y cargamos el recyclerview por primera vez.
//            //Este método ya no tiene sentido llamarlo desde el onCreate u onResume, pues necesitamos asegurarnos
//            //de haber cargado la base de datos antes de lanzarlo.
//            cargarView();
//        }


        //Método principal que se ejecutará en segundo plano.
        //El Void se corresponde al parámetro indicado en el encabezado de la clase.
        @Override
        protected String doInBackground(Void... voids) {
            //El mensaje que vamos a mostrar como notificación
            String mensaje = "";

            try {
                //Cargamos la base de datos.
                cargarPeliculas();
                cargarReparto();
//                cargarRepartoPelicula();

                //Si la carga no da ningún error inesperado...
                mensaje = "Lista de películas actualizada";

            }catch(Exception e) {
                //Si la carga da algún error
                mensaje = "Error en la actualización de la lista de películas";
            }

            //Lanzamos notificación.
            mNotificationManager.notify(001, mBuilder.build());
            return mensaje;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("FILTRO_CATEGORIA",  " " + filtroCategoria);
        // Rellenar base de datos con peliculas.
        cargarPeliculas();
        cargarReparto();
        cargarPeliculasReparto();

        // Cargar reparto y repartoPeliculas

        PeliculasDataSource peliculasDataSource = new PeliculasDataSource(getApplicationContext());
        peliculasDataSource.open();
        if(filtroCategoria == null){
                listaPeli = peliculasDataSource.getAll();
        } else {
            listaPeli = peliculasDataSource.getFilteredPeliculas(filtroCategoria);
        }
        peliculasDataSource.close();

        // Referencias a vistas
        listaPeliView = (RecyclerView)findViewById(R.id.recycleView);
        listaPeliView.setHasFixedSize(true);

        // Establecer el layout manager al recycler view, para colocar los items uno debajo del otro
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaPeliView.setLayoutManager(layoutManager);

        // Creamos el adapter
        ListaPeliculasAdapter lpAdapter = new ListaPeliculasAdapter(listaPeli,
                new ListaPeliculasAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Pelicula item) {
                        clickOnItem(item);
                    }
                });

        listaPeliView.setAdapter(lpAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setEnabled(false);
    }

    /**
     * Método para rellenar la lista de películas.
     */
//    private void rellenarLista(){
//        listaPeli = new ArrayList<>();
//        Categoria catAccion = new Categoria("Acción", "Pelis de acción");
//        Pelicula peli = new Pelicula("Tenet", "Una acción épica que gira en torno al espionaje internacional," +
//                " los viajes en el tiempo y la evolución, en la que un agente secreto debe prevenir la Tercera Guerra Mundial.",
//                catAccion, "150", "26/08/2020", "", "", "");
//        listaPeli.add(peli);
//    }

    /**
     * Método que será invocado cuando se haga click sobre un elemento
     * del recyclerView. Crea un intent para iniciar el MainActivity pasándole
     * la película.
     * @param pelicula
     */
    private void clickOnItem(Pelicula pelicula){
        // Paso el modo de apertura
        Intent intent = new Intent(MainRecycler.this, ShowMovie.class);
        intent.putExtra(PELICULA_SELECCIONADA, pelicula);
        // Transición de barrido a la activity para ver la pelicula.
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_ajustes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.ajustes){
            Intent intentSettingsActivity = new Intent(MainRecycler.this, SettingsActivity.class);
            startActivity(intentSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarReparto(){
        InputStream file = null;
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            file = getAssets().open("reparto.csv");
            bufferedReader = new BufferedReader(new InputStreamReader(file));
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                String data[] = line.split(";");
                if(data != null && data.length == 4){
                    Actor actor  = new Actor();
                    int idActor = Integer.parseInt(data[0]);
                    String nombre = data[1];
                    String imagen = data[2];
                    String urlIMDB = data[3];
                    actor.setId(idActor);
                    actor.setNombre(nombre);
                    actor.setImagen(imagen);
                    actor.setUrlIMDB(urlIMDB);
                    Log.d("CargarActores", actor.toString());
                    ActorsDataSource dataSource = new ActorsDataSource(getApplicationContext());
                    dataSource.open();
                    dataSource.insertActor(actor);
                    dataSource.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void cargarPeliculasReparto(){
        InputStream file = null;
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            file = getAssets().open("peliculas-reparto.csv");
            bufferedReader = new BufferedReader(new InputStreamReader(file));
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                String data[] = line.split(";");
                if(data != null && data.length == 3){
                    RepartoPelicula repartoPelicula = new RepartoPelicula();
                    long idPelicula = Long.parseLong(data[1]);
                    long idReparto = Long.parseLong(data[0]);
                    String personaje = data[2];
                    repartoPelicula.setPersonaje(personaje);
                    repartoPelicula.setId_actor(idReparto);
                    repartoPelicula.setId_pelicula(idPelicula);

                    RepartoPeliculaDataSource dataSource = new RepartoPeliculaDataSource(getApplicationContext());
                    dataSource.open();
                    dataSource.insertRepartoPelicula(repartoPelicula);
                    dataSource.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected void cargarPeliculas(){
        /*si una película le falta la caratual, el fondo o el trailer, le pongo unos por defecto. De esta manera me aseguro
        estos campos en las películas*/
        String Caratula_por_defecto="https://image.tmdb.org/t/p/original/jnFCk7qGGWop2DgfnJXeKLZFuBq.jpg\n";
        String fondo_por_defecto="https://image.tmdb.org/t/p/original/xJWPZIYOEFIjZpBL7SVBGnzRYXp.jpg\n";
        String trailer_por_defecto="https://www.youtube.com/watch?v=lpEJVgysiWs\n";
        Pelicula peli;

        listaPeli = new ArrayList<Pelicula>();
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            file = getAssets().open("peliculas.csv");
            reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(";");
                if (data != null && data.length >= 5) {
                    if (data.length==9) {
                        peli = new Pelicula(Integer.parseInt(data[0]), data[1], data[2],
                                new Categoria(data[3], ""), data[4], data[5],
                                data[6], data[7], data[8]);
                    } else {
                        peli = new Pelicula(Integer.parseInt(data[0]), data[1], data[2],
                                new Categoria(data[3], ""), data[4], data[5],
                                Caratula_por_defecto, fondo_por_defecto, trailer_por_defecto);
                    }
                    Log.d("cargarPeliculas", peli.toString());
                    // listaPeli.add(peli);
                    // Añadir película a la base de datos
                    PeliculasDataSource peliculasDataSource = new PeliculasDataSource(getApplicationContext());
                    peliculasDataSource.open();
                    peliculasDataSource.insertPelicula(peli);
                    peliculasDataSource.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}