package abel.project.twa.habana500.utils;

/**
 * AEC
 * @author Abel Alejandro Fleitas Perdomo
 * @since  2019
 **/

public class Book {

    int id;
    String nombre;
    int isMarcada;
    int paginaMarcada;

    public Book(int id, String nombre, int isMarcada, int paginaMarcada) {
        this.id = id;
        this.nombre = nombre;
        this.isMarcada = isMarcada;
        this.paginaMarcada = paginaMarcada;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int isMarcada() {
        return isMarcada;
    }

    public int getPaginaMarcada() {
        return paginaMarcada;
    }

    public void toStringObj(){
        System.err.println(nombre+" : "+isMarcada+" : "+paginaMarcada);
    }
}
