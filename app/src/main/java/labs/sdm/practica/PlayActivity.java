package labs.sdm.practica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }


    /*
    This method is executed when the activity is created to populate the ActionBar with actions
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_play, menu);
        menu.findItem(R.id.action_joker_telephone).setVisible(true);
        menu.findItem(R.id.action_joker_fifty).setVisible(true);
        menu.findItem(R.id.action_joker_audience).setVisible(true);
        menu.findItem(R.id.action_quit).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_joker_telephone:
                // User chose the "Credits" item, show the app Credits UI...
//                intent = new Intent(this,CreditsActivity.class);
//                startActivity(intent);
                Log.d("PlayActivity", "Phone Joker selected");
                return true;

            case R.id.action_joker_fifty:
                // User chose the "Credits" item, show the app Credits UI...
//                intent = new Intent(this,CreditsActivity.class);
//                startActivity(intent);
                Log.d("PlayActivity", "50-50 Joker selected");
                return true;

            case R.id.action_joker_audience:
                // User chose the "Credits" item, show the app Credits UI...
//                intent = new Intent(this,CreditsActivity.class);
//                startActivity(intent);
                Log.d("PlayActivity", "Audience Joker selected");
                return true;

            case R.id.action_quit:
                // User chose the "Credits" item, show the app Credits UI...
//                intent = new Intent(this,CreditsActivity.class);
//                startActivity(intent);
                Log.d("PlayActivity", "Quit selected");
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
