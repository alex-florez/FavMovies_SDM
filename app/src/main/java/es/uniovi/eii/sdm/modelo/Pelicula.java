package es.uniovi.eii.sdm.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Pelicula implements Parcelable {

    int id;


    String titulo;
    String argumento;
    Categoria categoria;
    String duracion;
    String fecha;

    String urlCaratula;
    String urlFondo;
    String urlTrailer;


    public Pelicula(int id, String titulo, String argumento, Categoria categoria, String duracion, String fecha,
                    String urlCaratula, String urlFondo, String urlTrailer) {
        this.id = id;
        this.titulo = titulo;
        this.argumento = argumento;
        this.categoria = categoria;
        this.duracion = duracion;
        this.fecha = fecha;

        this.urlCaratula = urlCaratula;
        this.urlFondo = urlFondo;
        this.urlTrailer = urlTrailer;
    }

    public Pelicula(){}

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getUrlCaratula() {
        return urlCaratula;
    }

    public String getUrlFondo() {
        return urlFondo;
    }

    public String getUrlTrailer() {
        return urlTrailer;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "titulo='" + titulo + '\'' +
                ", argumento='" + argumento + '\'' +
                ", categoria=" + categoria +
                ", duracion='" + duracion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", urlCaratula='" + urlCaratula + '\'' +
                '}';
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArgumento() {
        return argumento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setArgumento(String argumento) {
        this.argumento = argumento;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setUrlCaratula(String urlCaratula) {
        this.urlCaratula = urlCaratula;
    }

    public void setUrlFondo(String urlFondo) {
        this.urlFondo = urlFondo;
    }

    public void setUrlTrailer(String urlTrailer) {
        this.urlTrailer = urlTrailer;
    }


    // Implementación del Parcelable
    // *****************************

    protected Pelicula(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        argumento = in.readString();
        categoria = in.readParcelable(Categoria.class.getClassLoader());
        duracion = in.readString();
        fecha = in.readString();
        urlCaratula = in.readString();
        urlFondo = in.readString();
        urlTrailer = in.readString();
    }

    public static final Creator<Pelicula> CREATOR = new Creator<Pelicula>() {
        @Override
        public Pelicula createFromParcel(Parcel in) {
            return new Pelicula(in);
        }

        @Override
        public Pelicula[] newArray(int size) {
            return new Pelicula[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(titulo);
        dest.writeString(argumento);
        dest.writeParcelable(categoria, flags);
        dest.writeString(duracion);
        dest.writeString(fecha);
        dest.writeString(urlCaratula);
        dest.writeString(urlFondo);
        dest.writeString(urlTrailer);
    }


}
