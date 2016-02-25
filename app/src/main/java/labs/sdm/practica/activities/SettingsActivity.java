package labs.sdm.practica.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import labs.sdm.practica.R;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String[] values = {"0","1", "2", "3"};

    private int nHelpSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                nHelpSelected = position;
                break;
            case 1:
                nHelpSelected = position;
                break;
            case 2:
                nHelpSelected = position;
                break;
            case 3:
                nHelpSelected = position;
                break;

        }

        Log.d("SettingsActivity", "nHelpSelected: " + nHelpSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setSelection(nHelpSelected);
    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        nHelpSelected = prefs.getInt("helpNumber", 0);
        ((Spinner) findViewById(R.id.spinner)).setSelection(nHelpSelected);
        ((EditText) findViewById(R.id.etName)).setText(prefs.getString("userName", ""));
        ((EditText) findViewById(R.id.etAddFriend)).setText(prefs.getString("friendsName", ""));
        super.onResume();
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("helpNumber", nHelpSelected);
        editor.putString("userName",((EditText) findViewById(R.id.etName)).getText().toString() );
        editor.putString("friendsName",((EditText) findViewById(R.id.etAddFriend)).getText().toString() );
        editor.apply();
        super.onPause();
    }




}
