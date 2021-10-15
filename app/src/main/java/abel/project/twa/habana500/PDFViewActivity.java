/**
 * Copyright 2016 Bartosz Schiller
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package abel.project.twa.habana500;

/**
 * AEC
 * @author Abel Alejandro Fleitas Perdomo
 * @since  2019
 **/

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.List;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import abel.project.twa.habana500.basedatos.DataMarcadores;
import abel.project.twa.habana500.utils.Preferencias;

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private static final String TAG = PDFViewActivity.class.getSimpleName();
    private Bundle bundle;
    private PDFView pdfView;
    private String pdfFileName;
    Integer pageNumber;
    File root;
    AssetManager assetManager;
    ProgressDialog mProgressDialog;
    private SharedPreferences prefShared;
    private Preferencias preferencias;
    private PowerManager.WakeLock wakelock;
    private PowerManager pm;
    boolean shut_donw;
    private MenuItem marcador;
    private DataMarcadores data;

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;


    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private View mControlsView;
    private boolean mVisible;
    private Toolbar mToolbar;
    private ActionBar actionBar;
    private CoordinatorLayout linearLayout;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        bundle = getIntent().getExtras();

        linearLayout = (CoordinatorLayout) findViewById(R.id.activity_pdf);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(bundle.getString("name"));
        actionBar = getSupportActionBar();

        pageNumber = bundle.getInt("pagina");

        prefShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferencias = new Preferencias(prefShared.getString("orientacion","1"));

        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.setBackgroundColor(Color.LTGRAY);

        displayFromAsset(bundle.getString("archivo"));

        data = new DataMarcadores(getApplicationContext());

        mVisible = prefShared.getBoolean("full_screen",false);
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = pdfView;

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        shut_donw = prefShared.getBoolean("no_shut_down", false);
        if(shut_donw) {
            pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
            wakelock.acquire();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setup();
    }

    private void setup() {
        PDFBoxResourceLoader.init(getApplicationContext());
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        assetManager = getAssets();
        if (ContextCompat.checkSelfPermission(PDFViewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PDFViewActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        getSupportActionBar().setSubtitle(String.format("%s %s / %s","\n\n\n\n\n\n Página: ", page + 1, pageCount));
        if(data.isMarcadaByName(bundle.getString("name"))) {
            if (page != data.getPageByName(bundle.getString("name"))){
                marcador.setIcon(getResources().getDrawable(R.drawable.ic_marcador));
            }else{
                marcador.setIcon(getResources().getDrawable(R.drawable.ic_market));
            }
        }
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;
        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .enableDoubletap(true)
                .enableSwipe(true)
                .onLoad(this)
                .swipeHorizontal(preferencias.getScroll())
                //.pageSnap(true)
                //.autoSpacing(true)
                //.pageFling(true)
                //.nightMode( prefShared.getBoolean("nightMode",false))
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private String ruta;

        public DownloadTask(Context context) {
            this.context = context;
            ruta = "";
        }


        @Override
        protected String doInBackground(String... sUrl) {
            downloadPDF();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setIndeterminate(false);
        }

        @Override
        protected void onPostExecute(String result) {
            String[] partes = ruta.split("/");
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context,"Error en la descarga: "+result, Toast.LENGTH_LONG).show();
            else
                this.createSimpleDialog(ruta);
        }

        public void downloadPDF() {
                try {
                    String string = bundle.getString("archivo");
                    String[] parts = string.split("/");
                    String part2 = parts[1];
                    PDDocument document = PDDocument.load(assetManager.open(string));
                    String path = root.getAbsolutePath()+ "/"+part2;
                    String[] partes = path.split("/");
                    ruta = "El documento fue descargado hacia su carpeta "+partes[4]+"\n"+"con el nombre "+partes[5];
                    document.save(path);
                    document.close();
                } catch (IOException e) {
                    Log.e("PdfBox-Android-Sample", "Exception thrown while filling form fields", e);
                }
        }

        private void timeWait() {
            try {
                Thread.sleep(20);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void createSimpleDialog(String msj) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
            builder.setMessage(msj)
                    .setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if(prefShared.getBoolean("full_screen",false)){
            if (mVisible) {
                linearLayout.setFitsSystemWindows(false);
                hide();
            } else {
                linearLayout.setFitsSystemWindows(true);
                show();
            }
        }
    }

    private void hide() {
        if(prefShared.getBoolean("full_screen",false)){
            if (actionBar != null) {
                actionBar.hide();
            }
            mControlsView.setVisibility(View.GONE);
            mVisible = false;
            mHideHandler.removeCallbacks(mShowPart2Runnable);
            mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
        }
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    protected void onDestroy(){
        super.onDestroy();
        if(shut_donw) {
            this.wakelock.release();
        }
    }

    protected void onResume(){
        super.onResume();
        if(shut_donw) {
            wakelock.acquire();
        }
    }

    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        if(shut_donw) {
            this.wakelock.release();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        marcador = menu.findItem(R.id.marcador);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.pickFile:
                final DownloadTask  downloadTask = new DownloadTask(PDFViewActivity.this);
                mProgressDialog = new ProgressDialog(PDFViewActivity.this,R.style.MyDialogTheme);
                mProgressDialog.setMessage("Descargando archivo");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                downloadTask.execute();
                break;
            case R.id.marcador:
                if(!data.isMarcadaByName(bundle.getString("name"))){
                    marcador.setIcon(getResources().getDrawable(R.drawable.ic_market));
                    data.udateMarcador(bundle.getString("name"),1,pageNumber);
                }else if(data.isMarcadaByName(bundle.getString("name")) && data.getPageByName(bundle.getString("name")) != pageNumber){
                    marcador.setIcon(getResources().getDrawable(R.drawable.ic_market));
                    data.udateMarcador(bundle.getString("name"),1,pageNumber);
                }else{
                    marcador.setIcon(getResources().getDrawable(R.drawable.ic_marcador));
                    data.udateMarcador(bundle.getString("name"),0,pageNumber);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        switch (bundle.getString("desde")){
            case "Anuario":
                if(!data.isMarcadaByName(bundle.getString("name"))){
                    data.udateMarcador(bundle.getString("name"),0,pageNumber);
                }
                intent = new Intent(PDFViewActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        super.onBackPressed();
    }


}

