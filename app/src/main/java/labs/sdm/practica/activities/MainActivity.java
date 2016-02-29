package labs.sdm.practica.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import labs.sdm.practica.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Button bMainMenuPlay = (Button) findViewById(R.id.bPlay);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int mCurrentQuestion = prefs.getInt("currentQuestion", 1);

        if (mCurrentQuestion > 1) {
            bMainMenuPlay.setText(getResources().getString(R.string.resume));
        } else {
            bMainMenuPlay.setText(getResources().getString(R.string.bPlay));
            //reset Jokers according to settings
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("jokersSet", false);
            editor.apply();
        }
    }

    public void dashboardButtonClicked(View view){

        Intent intent = null;
        switch(view.getId()){
            case R.id.bPlay :
                intent = new Intent(this,PlayActivity.class);
                break;
            case R.id.bScores :
                intent = new Intent(this,ScoresActivity.class);
                break;
            case R.id.bSettings :
                intent = new Intent(this,SettingsActivity.class);
                break;

        }
        startActivity(intent);
    }

    /*
    This method is executed when the activity is created to populate the ActionBar with actions
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_credits).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_credits:
                // User chose the "Credits" item, show the app Credits UI...
                Intent intent = new Intent(this, CreditsActivity.class);
                startActivity(intent);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
