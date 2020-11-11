package es.uniovi.eii.sdm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import es.uniovi.eii.sdm.R;
import es.uniovi.eii.sdm.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {

    // Referencias a componentes
    EditText editNombre;
    EditText editDescripcion;
    TextView textTitle;
    Button btnOk;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        // Referencias a componentes
        editNombre = (EditText)findViewById(R.id.editTextNombreCat);
        editDescripcion = (EditText)findViewById(R.id.editTextDescripcionCat);
        textTitle = (TextView)findViewById(R.id.textCreacionCat);
        btnOk = (Button)findViewById(R.id.btnOk);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        // Recepción de los objetos que se le pasan a esta activity.
        Intent intent = getIntent();
        final int posCategoria = intent.getIntExtra(MainActivity.POS_CATEGORIA_SELECCIONADA, 0);
        Categoria categoriaSeleccionada = null;

        if(posCategoria > 0){
            categoriaSeleccionada = intent.getParcelableExtra(MainActivity.CATEGORIA_SELECCIONADA);
        }

        // Etiqueta de título distinta si se crea o se modifica la categoria
        if(posCategoria == 0){
            textTitle.setText("Creando nueva categoría.");
        } else {
            textTitle.setText("Modificando categoría existente");
            editNombre.setText(categoriaSeleccionada.getNombre());
            editDescripcion.setText(categoriaSeleccionada.getDescripcion());
            // No permitimos que se cambie el nombre de la categoría
            editNombre.setEnabled(false);
        }

        // Se acepta la operación
        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Recuperar datos de la categoría
                String nombre = editNombre.getText().toString();
                String descripcion = editDescripcion.getText().toString();
                Categoria catResultado = new Categoria(nombre, descripcion);

                Intent intentResultado = new Intent();
                intentResultado.putExtra(MainActivity.CATEGORIA_MODIFICADA, catResultado);
                intentResultado.putExtra(MainActivity.POS_CATEGORIA_SELECCIONADA, posCategoria);
                setResult(RESULT_OK, intentResultado);
                finish(); // Desapilamos esta activity.
            }
        });

        // Se cancela la operación
        btnCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}