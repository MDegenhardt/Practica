package labs.sdm.practica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String[] values = {"0","1", "2", "3"};

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
                Log.d("SettingsActivity", "Item " + position + " selected");
                break;
            case 1:
                Log.d("SettingsActivity", "Item " + position + " selected");
                break;
            case 2:
                Log.d("SettingsActivity", "Item " + position + " selected");
                break;
            case 3:
                Log.d("SettingsActivity", "Item " + position + " selected");
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setSelection(0);
    }

}
