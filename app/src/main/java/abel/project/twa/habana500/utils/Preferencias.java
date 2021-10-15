package abel.project.twa.habana500.utils;

/**
 * AEC
 * @author Abel Alejandro Fleitas Perdomo
 * @since  2019
 **/

public class Preferencias {

    private String orientacion;

    public Preferencias(String orientacion) {
        this.orientacion = orientacion;
    }

    public String getSumary()
    {
        if (orientacion.equals("1"))
        {
            return "Vertical";
        } else {
            return "Horizontal";
        }
    }

    public void setSumary(String value){
        this.orientacion = value;
    }

    public boolean getScroll(){

        if (orientacion.equals("1"))
        {
            return false;
        } else {
            return true;
        }
    }



}
