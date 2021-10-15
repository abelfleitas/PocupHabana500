package abel.project.twa.habana500.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

import abel.project.twa.habana500.utils.Book;

public class DataMarcadores {

    private SQLiteDatabase db;
    private Context context;
    private MarcadoresBD marcadoresBD;

    public DataMarcadores(Context context) {
        this.context = context;
        marcadoresBD = new MarcadoresBD(context,"Marcadores",null,1);
    }

    public void insertarData(ArrayList<Book> books){
        db = marcadoresBD.getWritableDatabase();
        int i = 0;
        for(Book book : books){
            db.execSQL("INSERT OR IGNORE INTO Marcadores (id,nombre,isMarcado,paginaMarcada) VALUES ('" + i + "','" + book.getNombre() +"','" + book.isMarcada() +"','"+book.getPaginaMarcada() +"')");
            i++;
        }
        db.close();
    }

    public int getPageById(int id){
        db = marcadoresBD.getReadableDatabase();
        int page = 0;
        String[] campos = new String[]{"paginaMarcada"};
        String[] bind = new String[]{""+id};
        Cursor c = db.query("Marcadores",campos,"id=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do page = c.getInt(0);
            while (c.moveToNext());
        }
        db.close();
        return page;
    }

    public int getPageByName(String name){
        db = marcadoresBD.getReadableDatabase();
        int page = 0;
        String[] campos = new String[]{"paginaMarcada"};
        String[] bind = new String[]{name};
        Cursor c = db.query("Marcadores",campos,"nombre=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                page = c.getInt(0);
            }
            while (c.moveToNext());
        }
        db.close();
        return page;
    }

    public void udateMarcador(String name,int value,int page){
        db = marcadoresBD.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("isMarcado",value);
        valores.put("paginaMarcada",page);
        String[] arg = new String[]{name};
        db.update("Marcadores",valores,"nombre=?",arg);
        db.close();
    }

    public boolean isMarcadaByName(String name){
        db = marcadoresBD.getReadableDatabase();
        String[] campos = new String[]{"id,nombre,isMarcado"};
        String[] bind = new String[]{name};
        Cursor c = db.query("Marcadores",campos,"nombre=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                if(c.getInt(2) == 1){
                    return true;
                }
            }
            while (c.moveToNext());
        }
        db.close();
        return false;
    }



}

