package labs.sdm.practica.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import labs.sdm.practica.R;
import labs.sdm.practica.databases.HighscoreSqlHelper;

public class ScoresActivity extends AppCompatActivity {

    // Data source for favourite quotations
    List<HashMap<String, String>> hashMapLocalList;
    List<HashMap<String, String>> hashMapFriendsList;

    // Adapter object linking the data source and the ListView
    SimpleAdapter adapterLocal, adapterFriends;
    // ListView object to display favourite quotations
    ListView scoresLocalListView, scoresFriendsListView;
    // Whether there is any highscore,
    // so the action to remove them from the database can appear on the ActionBar
    boolean clearAllLocalHighscores;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        context = this;


        scoresLocalListView = (ListView) findViewById(R.id.lvLocal);
        scoresFriendsListView = (ListView) findViewById(R.id.lvFriends);

        hashMapLocalList = new ArrayList<>();
        hashMapFriendsList = new ArrayList<>();

        hashMapLocalList.addAll(HighscoreSqlHelper.getInstance(this).getQuotations());

//        HashMap<String,String> item = new HashMap<>();
//        item.put("name", "Player1");
//        item.put("score", "300");
//        hashMapLocalList.add(item);
//        item = new HashMap<>();
//        item.put("name", "Player2");
//        item.put("score", "200");
//        hashMapLocalList.add(item);
//        item = new HashMap<>();
//        item.put("name", "Player3");
//        item.put("score", "100");
//        hashMapLocalList.add(item);

        hashMapFriendsList = hashMapLocalList;

        adapterLocal = new SimpleAdapter(this,hashMapLocalList,R.layout.list_view_row_scores,new String[]{"name", "score"},new int[]{R.id.tvScoresName, R.id.tvScoresScore} );
        adapterFriends = new SimpleAdapter(this,hashMapFriendsList,R.layout.list_view_row_scores,new String[]{"name", "score"},new int[]{R.id.tvScoresName, R.id.tvScoresScore} );

        scoresLocalListView.setAdapter(adapterLocal);
        scoresFriendsListView.setAdapter(adapterLocal);




        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec(("TAB1"));

        //Tab indicator specified as Label and Icon

        spec.setIndicator(getString(R.string.tabLocal),getResources().getDrawable(R.mipmap.ic_launcher));

        spec.setContent(R.id.linearLayout);

        host.addTab(spec);

        spec = host.newTabSpec(("TAB2"));

        //Tab indicator specified as Label and Icon

        spec.setIndicator(getString(R.string.tabFriends),getResources().getDrawable(R.mipmap.ic_launcher));

        spec.setContent(R.id.linearLayout2);

        host.addTab(spec);


        host.setCurrentTab(0);

        // If there is any quotation in the favourite list then set to true the flag
        // that controls whether to display the action for deleting all the quotations
        if (hashMapLocalList.size() > 0) {
            clearAllLocalHighscores = true;
        }
    }

    /*
This method is executed when the activity is created to populate the ActionBar with actions
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_scores, menu);
        menu.findItem(R.id.action_delete_scores).setVisible(clearAllLocalHighscores);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_delete_scores:

                // Build an AlertDialog to ask for confirmation before deleting all quotations
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Set the massage to display in the Dialog
                builder.setMessage(R.string.confirmation_clear);
                // Include a Button for handling positive confirmation: delete all quotations
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete all quotations from database
                        HighscoreSqlHelper.getInstance(context).clearAllHighscores();
                        // Clear the data source
                        hashMapLocalList.clear();
                        // Notify the adapter to update the ListView since the data source has changed
                        adapterLocal.notifyDataSetChanged();
                        // set to false the flag that controls whether to display
                        // the action for deleting all the quotations
                        clearAllLocalHighscores = false;
                        // Ask the system to rebuild the options of the ActionBar
                        supportInvalidateOptionsMenu();
                    }
                });
                // Include a Button for handling negative confirmation: do not delete all quotations
                // No need for an onClickListener() here, as no action will take place
                builder.setNegativeButton(android.R.string.no, null);
                // Create and show the Dialog
                builder.create().show();

                // Return true as we handled the event
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
