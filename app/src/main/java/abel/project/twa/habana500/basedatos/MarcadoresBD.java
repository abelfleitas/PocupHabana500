package abel.project.twa.habana500.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MarcadoresBD extends SQLiteOpenHelper{


    public MarcadoresBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {

        bd.execSQL("CREATE TABLE Marcadores (" +
                "id integer PRIMARY KEY NOT NULL," +
                "nombre varchar NOT NULL," +
                "isMarcado integer NOT NULL," +
                "paginaMarcada integer NOT NULL," +
                "UNIQUE(id,nombre)"+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {

        bd.execSQL("DROP TABLE IF EXISTS Marcadores");
        bd.execSQL("CREATE TABLE Marcadores (" +
                "id integer PRIMARY KEY NOT NULL," +
                "nombre varchar NOT NULL," +
                "isMarcado integer NOT NULL," +
                "paginaMarcada integer NOT NULL," +
                "UNIQUE(id,nombre)"+
                ")");
    }
}
