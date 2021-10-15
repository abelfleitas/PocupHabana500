package abel.project.twa.habana500;

/**
 * AEC
 * @author Abel Alejandro Fleitas Perdomo
 * @since  2019
 **/

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import abel.project.twa.habana500.utils.LanzarToast;
import abel.project.twa.habana500.utils.Preferencias;

public class SettingsActivity extends AppCompatActivity {

    private Preference shortcut;
    private Preferencias preferencias;
    private SharedPreferences prefShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new GeneralPreferenceFragment())
                .commit();
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @SuppressLint("ValidFragment")
    private class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferencias);
            setHasOptionsMenu(true);
            onCreateView(getLayoutInflater(),null,savedInstanceState);
            prefShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            preferencias = new Preferencias(prefShared.getString("orientacion","1"));
            shortcut = findPreference("pref_homescreen_shortcut");
            bindPreferenceSummaryToValue(findPreference("orientacion"));
            shortcut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent shortcutIntent = new Intent(SettingsActivity.this, MainActivity.class);
                    shortcutIntent.setAction("android.intent.action.MAIN");
                    Intent addIntent = new Intent();
                    addIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
                    addIntent.putExtra("android.intent.extra.shortcut.NAME", getResources().getString(R.string.app_name));
                    addIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    addIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    addIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(getApplicationContext(),R.mipmap.ic_launcher));
                    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                    getApplicationContext().sendBroadcast(addIntent);
                    LanzarToast.showToastOficial(SettingsActivity.this,R.string.addedHomeScreenShortcout);
                    return true;
                }
            });

        }
    }

    private  void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(),"1"));

    }

    private  Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            if(preference instanceof ListPreference && preference.getKey().equalsIgnoreCase("orientacion")){
                String va = value.toString();
                preferencias.setSumary(va);
                preference.setSummary(preferencias.getSumary());
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        SettingsActivity.this.finish();
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
