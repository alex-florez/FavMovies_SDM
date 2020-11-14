package es.uniovi.eii.sdm.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase que implementa el patrón singleton para crear un solo
 * cliente de Retrofit.
 */
public class RetrofitClient {
    private static Retrofit retrofit;
    private RetrofitClient(){}

    public static Retrofit getClient(String baseUrl){
        if(retrofit == null){
            // Instanciar el cliente retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
