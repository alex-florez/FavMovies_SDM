package es.uniovi.eii.sdm.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.fragments.ActoresFragment;
import es.uniovi.eii.sdm.fragments.ArgumentoFragment;
import es.uniovi.eii.sdm.fragments.InfoFragment;
import es.uniovi.eii.sdm.modelo.Pelicula;
import es.uniovi.eii.sdm.util.Conexion;

public class ShowMovie extends AppCompatActivity {

    private Pelicula pelicula;

    // Componentes
    private CollapsingToolbarLayout toolBarLayout;
    private Toolbar toolbar;
    private ImageView imagenFondo;
    private TextView categoria;
    private TextView estreno;
    private TextView duracion;
    private TextView argumento;
    private ImageView caratula;

    // Listener para la navegación por los botones
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (pelicula != null) {
                switch (item.getItemId()) {
                    case R.id.navigation_info:
                        //Creamos el framento de información
                        InfoFragment info = new InfoFragment();
                        Bundle args = new Bundle();
                        args.putString(InfoFragment.ESTRENO, pelicula.getFecha());
                        args.putString(InfoFragment.DURACION, pelicula.getDuracion());
                        args.putString(InfoFragment.CARATULA, pelicula.getUrlCaratula());
                        info.setArguments(args);
                        // Reemplazar el fragment en el frame layout de la botonera
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, info).commit();
                        return true;

                    case R.id.navigation_actores:
                        ActoresFragment actores = new ActoresFragment();
                        Bundle args1 = new Bundle();
                        args1.putInt("id_pelicula", pelicula.getId());
                        actores.setArguments(args1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, actores)
                                .commit();
                        return true;
                    case R.id.navigation_argumento:
                        ArgumentoFragment argumento = new ArgumentoFragment();
                        args = new Bundle();
                        args.putString(ArgumentoFragment.ARGUMENTO, pelicula.getArgumento());
                        argumento.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, argumento)
                                .commit();
                        return true;
                }
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        imagenFondo = (ImageView)findViewById(R.id.imagenFondo);
        caratula = (ImageView)findViewById(R.id.caratula);

        categoria = findViewById(R.id.categoria);
        duracion = (TextView)findViewById(R.id.duracion);
        estreno = (TextView)findViewById(R.id.estreno);
        argumento = (TextView)findViewById(R.id.argumento);

        // Fragments
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Recepción de datos
        Intent intentPeli = getIntent();
        pelicula = intentPeli.getParcelableExtra(MainRecycler.PELICULA_SELECCIONADA);

        if(pelicula != null)
            mostrarDatos(pelicula);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verTrailer(pelicula.getUrlTrailer());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

//        if(id == R.id.action_settings){
//            return true;
//        }

        if(id == R.id.compartir){
            Conexion conexion = new Conexion(getApplicationContext());
            if(conexion.compruebaConexion()){
                compartirPeli();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_conexion, Toast.LENGTH_LONG)
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void compartirPeli(){
        Intent intSend = new Intent(Intent.ACTION_SEND);
        intSend.setType("text/plain");
        intSend.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.subject_compartir) + " : " + pelicula.getTitulo());
        intSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.titulo)
                +": "+pelicula.getTitulo()+"\n"+
                getString(R.string.contenido)
                +": "+pelicula.getArgumento());
        /* iniciamos la actividad */
            /* puede haber más de una aplicacion a la que hacer un ACTION_SEND,
               nos sale un ventana que nos permite elegir una.
               Si no lo pongo y no hay activity disponible, pueda dar un error */
        Intent shareIntent=Intent.createChooser(intSend, null);
        startActivity(shareIntent);
    }

    private void verTrailer(String urlTrailer){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlTrailer)));
    }


    /**
     * Carga los datos de la instancia de la película en los componentes de la activity
     * @param pelicula
     */
    public void mostrarDatos(Pelicula pelicula){
        if(!pelicula.getTitulo().isEmpty()){
            String fecha = pelicula.getFecha();
            toolBarLayout.setTitle(pelicula.getTitulo() + " (" + fecha.substring(fecha.lastIndexOf('/') + 1) + ")");
            // Imagen de fondo
            Picasso.get()
                    .load(pelicula.getUrlFondo()).into(imagenFondo);

            // Fragment por defecto
            InfoFragment infoFragment = new InfoFragment();
            Bundle args = new Bundle();
            args.putString(InfoFragment.ESTRENO, pelicula.getFecha());
            args.putString(InfoFragment.DURACION, pelicula.getDuracion());
            args.putString(InfoFragment.CARATULA, pelicula.getUrlCaratula());
            infoFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, infoFragment).commit();
        }
    }
}