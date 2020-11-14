package es.uniovi.eii.sdm.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Actor implements Parcelable {

    private int id;
    private String nombre;
    private String nombre_personaje;
    private String imagen;
    private String urlIMDB;

    public Actor(){}

    public Actor(int id, String nombre, String imagen, String urlIMDB){
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.urlIMDB = urlIMDB;
    }

    public Actor(int id, String nombre, String personake, String imagen, String imdb){
        this.id = id;
        this.nombre = nombre;
        this.nombre_personaje = personake;
        this.imagen = imagen;
        this.urlIMDB = imdb;
    }

    public String getNombre_personaje() {
        return nombre_personaje;
    }

    public void setNombre_personaje(String nombre_personaje) {
        this.nombre_personaje = nombre_personaje;
    }

    protected Actor(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        nombre_personaje = in.readString();
        imagen = in.readString();
        urlIMDB = in.readString();
    }

    public static final Creator<Actor> CREATOR = new Creator<Actor>() {
        @Override
        public Actor createFromParcel(Parcel in) {
            return new Actor(in);
        }

        @Override
        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getUrlIMDB() {
        return urlIMDB;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setUrlIMDB(String urlIMDB) {
        this.urlIMDB = urlIMDB;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", imagen='" + imagen + '\'' +
                ", urlIMDB='" + urlIMDB + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nombre);
        dest.writeString(nombre_personaje);
        dest.writeString(imagen);
        dest.writeString(urlIMDB);
    }
}
