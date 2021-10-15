package abel.project.twa.habana500;

/**
 * AEC
 * @author Abel Alejandro Fleitas Perdomo
 * @since  2019
 **/

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import abel.project.twa.habana500.basedatos.DataMarcadores;
import abel.project.twa.habana500.utils.Book;

public class SplashActivity extends AppCompatActivity {

    TextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah);
        tvLoading = (TextView) findViewById(R.id.title);
        SplashActivity.this.init();
    }

    public void init() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                return null;
            }

            protected void onPreExecute() {
                SplashActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        /*SplashActivity.this.loader.start();
                        SplashActivity.this.loader.setOnStateChangeListener(new OnStateChangeListener() {
                            public void onStateChange(int state) {
                                if (state == 2) {
                                    SplashActivity.this.tvLoading.setVisibility(View.VISIBLE);
                                }
                                if (state == 3) {
                                    SplashActivity.this.tvLoading.setVisibility(View.VISIBLE);
                                }
                            }
                        });*/
                    }
                });
            }

            protected void onPostExecute(Void aVoid) {
                SplashActivity.this.createBd();
                SplashActivity.this.showSuccessfullySavedMessage();
            }
        }.execute(new Void[0]);
    }

    public void showSuccessfullySavedMessage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 2500);
    }

    protected void onResume() {
        getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        super.onResume();
    }

    private void createBd(){
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book(0,getResources().getString(R.string.intro),0,0));
        books.add(new Book(1,getResources().getString(R.string.parte1),0,0));
        books.add(new Book(2,getResources().getString(R.string.parte2),0,0));
        books.add(new Book(3,getResources().getString(R.string.parte3),0,0));
        books.add(new Book(4,getResources().getString(R.string.parte4),0,0));
        books.add(new Book(5,getResources().getString(R.string.bibliografia),0,0));
        books.add(new Book(6,getResources().getString(R.string.anexo),0,0));

        DataMarcadores marcadores = new DataMarcadores(getApplicationContext());
        marcadores.insertarData(books);
    }

}
