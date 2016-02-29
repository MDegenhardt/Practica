package labs.sdm.practica.activities;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import labs.sdm.practica.R;
import labs.sdm.practica.tasks.AddFriendAsyncTask;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    // Holds reference to the asynchronous task
    AddFriendAsyncTask task;
    private int nHelpSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String s0 = getResources().getString(R.string.sv0NoJokers);
        String s1 = getResources().getString(R.string.sv1_50);
        String s2 = getResources().getString(R.string.sv2_tel);
        String s3 = getResources().getString(R.string.sv3_aud);
        String[] str = {s0, s1, s2, s3};

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, str);
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

        Toast.makeText(this, R.string.warningJokerSelect, Toast.LENGTH_LONG).show();

//        Log.d("SettingsActivity", "nHelpSelected: " + nHelpSelected);
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
        ((EditText) findViewById(R.id.etName)).setText(prefs.getString("userName", getResources().getString(R.string.anonymousUser)));
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


    public void addFriendClicked(View view){



        EditText et = (EditText) findViewById(R.id.etAddFriend);
        String friendName = ((EditText) findViewById(R.id.etAddFriend)).getText().toString();
        String userName = ((EditText) findViewById(R.id.etName)).getText().toString();

        if (friendName.equals("")) {
            Toast.makeText(this, R.string.warningFriendName, Toast.LENGTH_SHORT).show();
        } else if (isConnectionAvailable()) {

            //new friend task...
            task = new AddFriendAsyncTask();
            // Start the task
            task.execute(userName,friendName);
            et.setText("");
            Toast.makeText(this, String.format(getResources().getString(R.string.addedFriend), friendName, userName), Toast.LENGTH_LONG).show();
        }
        // There is no Internet connection available, so inform the user about that
        else {
            Toast.makeText(this, R.string.connection_not_available, Toast.LENGTH_SHORT).show();
        }


    }

    /*
    Check whether Internet connectivity is available
*/
    private boolean isConnectionAvailable() {

        // Get a reference to the ConnectivityManager
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // Get information for the current default data network
        NetworkInfo info = manager.getActiveNetworkInfo();
        // Return true if there is network connectivity
        return ((info != null) && info.isConnected());
    }


}
