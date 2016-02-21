package labs.sdm.practica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {

    // Data source for favourite quotations
    List<HashMap<String, String>> hashMapLocalList;
    List<HashMap<String, String>> hashMapFriendsList;

    // Adapter object linking the data source and the ListView
    SimpleAdapter adapterLocal, adapterFriends;
    // ListView object to display favourite quotations
    ListView scoresLocalListView, scoresFriendsListView;



    // Whether there is any favourite quotation,
    // so the action to remove them from the database can appear on the ActionBar
    boolean clearLocalScores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);


        scoresLocalListView = (ListView) findViewById(R.id.lvLocal);
        scoresFriendsListView = (ListView) findViewById(R.id.lvFriends);

        hashMapLocalList = new ArrayList<>();
        hashMapFriendsList = new ArrayList<>();

        HashMap<String,String> item = new HashMap<>();
        item.put("name", "Player1");
        item.put("score", "300");
        hashMapLocalList.add(item);
        item = new HashMap<>();
        item.put("name", "Player2");
        item.put("score", "200");
        hashMapLocalList.add(item);
        item = new HashMap<>();
        item.put("name", "Player3");
        item.put("score", "100");
        hashMapLocalList.add(item);

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
    }

    /*
This method is executed when the activity is created to populate the ActionBar with actions
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_scores, menu);
        menu.findItem(R.id.action_delete_scores).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_delete_scores:
                // User chose the "Credits" item, show the app Credits UI...
//                intent = new Intent(this,CreditsActivity.class);
//                startActivity(intent);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
