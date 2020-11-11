package es.uniovi.eii.sdm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Conexion {

    private Context mContexto;

    // Contexto de la conexión
    public Conexion(Context mContexto){
        this.mContexto = mContexto;
    }

    /**
     * Comprueba si el dispositivo está conectado a una red y
     * dispone de conexión.
     * @return
     */
    public boolean compruebaConexion(){
        boolean conectado = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        conectado = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return conectado;
    }
}
