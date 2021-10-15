package abel.project.twa.habana500;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import abel.project.twa.habana500.utils.LanzarToast;

public class ContactoActivity extends AppCompatActivity {

    LinearLayout email,webpage,tel,fb,twi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = (LinearLayout) findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.co_email)});
                emailIntent.putExtra(Intent.EXTRA_CC, new String[]{});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                emailIntent.setType("message/rfc822");
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(emailIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(Intent.createChooser(emailIntent,"Seleccione la app"));
                } else {
                    LanzarToast.showToastOficial(ContactoActivity.this, R.string.call_email);
                }
            }
        });
        webpage = (LinearLayout) findViewById(R.id.webpage);
        webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.co_web)));
                startActivity(i);
            }
        });

        tel = (LinearLayout) findViewById(R.id.tel);
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.DIAL",
                        Uri.parse("tel:" + getString(R.string.co_telreal)));
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(intent);
                } else {
                    LanzarToast.showToastOficial(ContactoActivity.this, R.string.call_email);
                }
            }
        });

        fb = (LinearLayout) findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.co_facebook)));
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ContactoActivity.this, MainActivity.class));
        ContactoActivity.this.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
