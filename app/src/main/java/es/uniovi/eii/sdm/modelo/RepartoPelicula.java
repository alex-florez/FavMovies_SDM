package es.uniovi.eii.sdm.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class RepartoPelicula  implements Parcelable {

    private long id_pelicula;
    private long id_actor;
    private String personaje;

    public RepartoPelicula(){}

    public  RepartoPelicula(long id_pelicula, long id_actor, String personaje){
        this.id_pelicula = id_pelicula;
        this.id_actor = id_actor;
        this.personaje = personaje;
    }


    protected RepartoPelicula(Parcel in) {
        id_pelicula = in.readLong();
        id_actor = in.readLong();
        personaje = in.readString();
    }

    public static final Creator<RepartoPelicula> CREATOR = new Creator<RepartoPelicula>() {
        @Override
        public RepartoPelicula createFromParcel(Parcel in) {
            return new RepartoPelicula(in);
        }

        @Override
        public RepartoPelicula[] newArray(int size) {
            return new RepartoPelicula[size];
        }
    };

    public long getId_pelicula() {
        return id_pelicula;
    }

    public long getId_actor() {
        return id_actor;
    }

    public String getPersonaje() {
        return personaje;
    }

    public void setId_pelicula(long id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public void setId_actor(long id_actor) {
        this.id_actor = id_actor;
    }

    public void setPersonaje(String personaje) {
        this.personaje = personaje;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id_pelicula);
        dest.writeLong(id_actor);
        dest.writeString(personaje);
    }
}
