package abel.project.twa.habana500;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Suggestion
        LinearLayout sug=(LinearLayout) findViewById(R.id.linearItemSugerencia);
        sug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.abel_email)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sugerencia) + " " + getString(R.string.app_name));
                emailIntent.setType("message/rfc822");
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(emailIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.choose)));
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noappemail), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Problem
        LinearLayout problem=(LinearLayout) findViewById(R.id.linearItemReporte);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.abel_email)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.error_app_version));
                emailIntent.setType("message/rfc822");
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(emailIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.choose)));
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noappemail), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AboutActivity.this, MainActivity.class));
        AboutActivity.this.finish();
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
