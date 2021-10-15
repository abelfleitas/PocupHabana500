package abel.project.twa.habana500;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import abel.project.twa.habana500.basedatos.DataMarcadores;
import cn.refactor.lib.colordialog.PromptDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private DataMarcadores marcadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        marcadores = new DataMarcadores(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view_right);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            PromptDialog dialog = new PromptDialog(this);
            dialog.setDialogType(PromptDialog.DIALOG_TYPE_HELP);
            dialog.setAnimationEnable(true);
            dialog.setTitleText((int) R.string.confirm);
            dialog.setContentText((int) R.string.confirm_exit);
            dialog.setCancelable(false);
            dialog.setPositiveListener(this.getString(R.string.aceptar), new PromptDialog.OnPositiveListener() {
                public void onClick(PromptDialog dialog) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setNegativeListener(this.getString(R.string.md_cancel_label), new PromptDialog.OnNegativeListener() {
                public void onClick(PromptDialog dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_opciones) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            MainActivity.this.finish();
            return true;
        }
        else if (id == R.id.menu_acercade) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            MainActivity.this.finish();
            return true;
        }
        else if (id == R.id.menu_contacto) {
            startActivity(new Intent(MainActivity.this, ContactoActivity.class));
            MainActivity.this.finish();
            return true;
        }
        /*else if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
            MainActivity.this.finish();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.intro){
            Intent intent = new Intent(MainActivity.this,PDFViewActivity.class);
            intent.putExtra("name",getResources().getString(R.string.intro));
            intent.putExtra("desde","Anuario");
            intent.putExtra("archivo","habana_500/introduccion_compressed.pdf");
            intent.putExtra("pagina",marcadores.getPageById(0));
            startActivity(intent);
            finish();
        }
        else if(id == R.id.parte1){
            Intent intent = new Intent(MainActivity.this,PDFViewActivity.class);
            intent.putExtra("name",getResources().getString(R.string.parte1));
            intent.putExtra("desde","Anuario");
            intent.putExtra("archivo","habana_500/parte1_compressed.pdf");
            intent.putExtra("pagina",marcadores.getPageById(1));
            startActivity(intent);
            finish();
        }
        else if(id == R.id.parte2){
            Intent intent = new Intent(MainActivity.this,PDFViewActivity.class);
            intent.putExtra("name",getResources().getString(R.string.parte2));
            intent.putExtra("desde","Anuario");
            intent.putExtra("archivo","habana_500/parte2_compressed.pdf");
            intent.putExtra("pagina",marcadores.getPageById(2));
            startActivity(intent);
            finish();
        }
        else if(id == R.id.parte3){
            Intent intent = new Intent(MainActivity.this,PDFViewActivity.class);
            intent.putExtra("name",getResources().getString(R.string.parte3));
            intent.putExtra("desde","Anuario");
            intent.putExtra("archivo","habana_500/parte3_compressed.pdf");
            intent.putExtra("pagina",marcadores.getPageById(3));
            startActivity(intent);
            finish();
        }
        else if(id == R.id.parte4){
            Intent intent = new Intent(MainActivity.this,PDFViewActivity.class);
            intent.putExtra("name",getResources().getString(R.string.parte4));
            intent.putExtra("desde","Anuario");
            intent.putExtra("archivo","habana_500/parte4_compressed.pdf");
            intent.putExtra("pagina",marcadores.getPageById(4));
            startActivity(intent);
            finish();
        }
        else if(id == R.id.bibliografia){
            Intent intent = new Intent(MainActivity.this,PDFViewActivity.class);
            intent.putExtra("name",getResources().getString(R.string.bibliografia));
            intent.putExtra("desde","Anuario");
            intent.putExtra("archivo","habana_500/bibliografia_compressed.pdf");
            intent.putExtra("pagina",marcadores.getPageById(5));
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this,PDFViewActivity.class);
            intent.putExtra("name",getResources().getString(R.string.anexo));
            intent.putExtra("desde","Anuario");
            intent.putExtra("archivo","habana_500/anexos_compressed.pdf");
            intent.putExtra("pagina",marcadores.getPageById(6));
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
