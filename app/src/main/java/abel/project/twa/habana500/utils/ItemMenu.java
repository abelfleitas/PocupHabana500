package abel.project.twa.habana500.utils;

/**
 * AEC
 * @author Abel Alejandro Fleitas Perdomo
 * @since  2019
 **/

public class ItemMenu {

    private int imagen;
    private String activity;

    public ItemMenu(int imagen, String activity) {
        this.imagen = imagen;
        this.activity = activity;
    }

    public int getImagen() {
        return imagen;
    }

    public String getActivity() {
        return activity;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
