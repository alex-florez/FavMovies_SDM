package es.uniovi.eii.sdm.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.modelo.Categoria;
import es.uniovi.eii.sdm.modelo.Pelicula;

public class MainActivity extends AppCompatActivity {

    private final int SHOW_TOASTS = 0;

    // Constantes para los parámetros entre activities - identificadores de intents
    public static final String NOMBRE_CATEGORIA = "categoria";
    public static final String POS_CATEGORIA_SELECCIONADA = "posCategoria";
    public static final String CATEGORIA_SELECCIONADA = "catSeleccionada";
    public static final String CATEGORIA_MODIFICADA = "catModificada";

    // Variable que indica si se está creando una nueva categoría.
    private boolean creandoCategoria;

    // identificador de la activity para gestionar una categoría
    private static final int GESTION_CATEGORIA = 1;

    // Listado de categorias
    private List<Categoria> listaCategorias;

    // Declarar componentes
    private Spinner spinnerCat;
    private EditText editTitulo;
    private EditText editArg;
    private EditText editDuracion;
    private EditText editFecha;


    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToast(getString(R.string.OnCreate), Toast.LENGTH_SHORT);

        // Recoger referencias a cada componente del layout.
        spinnerCat = (Spinner)findViewById(R.id.spinnerCat);
        editTitulo = (EditText)findViewById(R.id.editTitulo);
        editArg = (EditText)findViewById(R.id.editArg);
        editDuracion = (EditText)findViewById(R.id.editDuracion);
        editFecha = (EditText)findViewById(R.id.editFecha);

        // Inicializar modelo de datos
        listaCategorias = new ArrayList<Categoria>();
        listaCategorias.add(new Categoria("Acción", "Películas de acción"));
        listaCategorias.add(new Categoria("Romántica", "Películas románticas"));
        listaCategorias.add(new Categoria("Drama", "Películas de drama"));

        // Inicializar spinner
        Spinner spinner = (Spinner)findViewById(R.id.spinnerCat);
        introListaSpinner(spinner, listaCategorias);

        // Registrar listeners en los componentes
        // Listener onClick para el botón Guardar
        Button btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()) // Validamos los datos antes de guardar.
                    createSnackbar(R.string.msgGuardar, Snackbar.LENGTH_SHORT).show();
            }
        });

        // Listener onClick para el botón de modificar categoría.
        ImageButton btnModCat = (ImageButton)findViewById(R.id.btnModCat);
        btnModCat.setOnClickListener(new View.OnClickListener(){
            private Snackbar msgSnackbar;
            @Override
            public void onClick(View v) {
                msgSnackbar = createSnackbar(R.string.msgNuevaCat, Snackbar.LENGTH_SHORT);
                msgSnackbar.setAction(R.string.ok, new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            modificarCategoria();
                        }
                });
                msgSnackbar.show();
            }
        });

        btnModCat.setVisibility(View.GONE);

        // Recepción del intent del MainRecycler
        receiveMovie();
    }

    /**
     * Método que se encarga de la recepción del objeto intent
     * enviado desde la activity MainRecycler.
     */
    private void receiveMovie(){
        Intent intentRecycler = getIntent();
        Pelicula pelicula = intentRecycler.getParcelableExtra(MainRecycler.PELICULA_SELECCIONADA);
        editTitulo.setText(pelicula.getTitulo());
        editArg.setText(pelicula.getArgumento());
        editDuracion.setText(pelicula.getDuracion());
        editFecha.setText(pelicula.getFecha());
        // Obtenemos la posición de la categoría de la pelicula en el spinner
        int posCategoria = listaCategorias.indexOf(pelicula.getCategoria());
        spinnerCat.setSelection(posCategoria + 1);
    }

    /**
     * Método que será invocado cuando se haga click sobre el botón
     * para modificar una categoría.
     */
    private void modificarCategoria(){
        Intent intCategoria = new Intent(MainActivity.this, CategoriaActivity.class);
        intCategoria.putExtra(POS_CATEGORIA_SELECCIONADA, spinnerCat.getSelectedItemPosition());

        creandoCategoria = true;
        if(spinnerCat.getSelectedItemPosition() > 0){
            creandoCategoria = false;
            intCategoria.putExtra(CATEGORIA_SELECCIONADA,
                    listaCategorias.get(spinnerCat.getSelectedItemPosition() - 1));
        }
        // Lanzamos la 2a activity para gestionar la categoría esperando por resultado.
        startActivityForResult(intCategoria, GESTION_CATEGORIA);
    }


    // Método invocado cuando la activity main reciba un resultado.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if(requestCode == GESTION_CATEGORIA){ // Resultado proviene de la activity para gestionar categoría
            if(resultCode == RESULT_OK){ // Resultado correcto
                // Recogemos la categoría
                Categoria newCategoria = data.getParcelableExtra(MainActivity.CATEGORIA_MODIFICADA);
                if(creandoCategoria){ // Es una nueva categoría
                    listaCategorias.add(newCategoria);
                    introListaSpinner(spinnerCat, listaCategorias); // Actualizamos el spinner
                } else { // Es una categoría existente que se ha modificado
                    // Buscamos en la lista de categorías la que hemos modificado
                   // Obtenemos la posición seleccionada en el spinner del intent
                    int pos = data.getIntExtra(MainActivity.POS_CATEGORIA_SELECCIONADA, 0);
                    listaCategorias.get(pos-1).setDescripcion(newCategoria.getDescripcion());
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        createToast(getString(R.string.OnStart), Toast.LENGTH_SHORT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        createToast(getString(R.string.OnStop), Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        createToast(getString(R.string.OnPause), Toast.LENGTH_SHORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createToast(getString(R.string.OnResume), Toast.LENGTH_SHORT);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        createToast(getString(R.string.OnRestart), Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        createToast(getString(R.string.OnDestroy), Toast.LENGTH_SHORT);
    }


// Métodos auxiliares privados
    // ***************************

    /**
     * Cream un widget Toast con un mensaje y una duración
     * determinadas y lo muestra.
     * @param msg
     * @param duration
     */
    private void createToast(String msg, int duration){
        if(SHOW_TOASTS > 0)
            Toast.makeText(getApplicationContext(), msg, duration).show();
    }


    private Snackbar createSnackbar(int msg_value, int duration){
        return Snackbar.make(findViewById(R.id.layoutPrincipal), msg_value, duration);
    }

    /**
     * Método para rellenar el spinner pasado como parámetro con la lista
     * de objetos categoría del modelo.
     * @param spinner
     * @param listaCategorias
     */
    private void introListaSpinner(Spinner spinner, List<Categoria> listaCategorias){
        List<String> nombres = new ArrayList<String>();
        nombres.add("Sin definir");
        for(Categoria cat : listaCategorias){
            nombres.add(cat.getNombre());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, nombres);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }


    /**
     * Método encargado de validar las entradas de los
     * inputs de esta activity. Para cada entrada no válida,
     * genera el correspondiente mensaje emergente.
     */
    private boolean validateData(){
        boolean allCorrect = true;
        // Referencias a los componentes
        EditText editTitulo = (EditText)findViewById(R.id.editTitulo);
        EditText editArg = (EditText)findViewById(R.id.editArg);
        EditText duracion =  (EditText)findViewById(R.id.editDuracion);
        EditText fecha = (EditText)findViewById(R.id.editFecha);
        Spinner spinnerCat = (Spinner)findViewById(R.id.spinnerCat);

        // validar título
        if(editTitulo.getText().toString().length() == 0){
            allCorrect = false;
            showError(R.string.errorTituloVacio, editTitulo);
            //createSnackbar(R.string.errorTituloVacio, Snackbar.LENGTH_SHORT).show();
        }
        // validar argumento
        if(editArg.getText().toString().isEmpty()){
            allCorrect = false;
//            createSnackbar(R.string.errorArgVacio, Snackbar.LENGTH_SHORT).show();
            showError(R.string.errorArgVacio, editArg);
        }
        // validar categoría
//        if(spinnerCat.getSelectedItemPosition() == 0){
//            allCorrect = false;
//            createSnackbar(R.string.errorCatSinDefinir, Snackbar.LENGTH_SHORT).show();
//        }

        // validar duración
        if(duracion.getText().toString().length() == 0){
            allCorrect = false;
//            createSnackbar(R.string.errorDuracionVacio, Snackbar.LENGTH_SHORT).show();
            showError(R.string.errorDuracionVacio, duracion);
        }
        // validar fecha
        if(fecha.getText().toString().length() == 0){
            allCorrect = false;
//            createSnackbar(R.string.errorFechaVacio, Snackbar.LENGTH_SHORT).show();
            showError(R.string.errorFechaVacio, fecha);
        }
        return allCorrect;
    }

    /**
     * Muestra un mensaje de error con la cadena pasada como parámetro
     * obteniendo el foco para el EditText pasado como parámetro.
     * @param textCode
     * @param editText
     */
    private void showError(int textCode, EditText editText){
        editText.requestFocus();
        editText.setError(getString(textCode));
    }
}