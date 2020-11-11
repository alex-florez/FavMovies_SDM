package es.uniovi.eii.sdm.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import es.uniovi.eii.sdm.R;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        // Fragment para los ajustes
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        // Flechita para ir hacia atrás (modificar manifiesto)
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onPause(){
        super.onPause();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        name = sharedPreferences.getString("keyCategoria", "");
        Log.i("Categoría", name);

        MainRecycler.filtroCategoria = name;
    }

    /**
     * Fragment específico para las preferencias
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // Establecer el layout para estas preferencias
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}