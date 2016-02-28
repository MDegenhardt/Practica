package labs.sdm.practica.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import labs.sdm.practica.R;
import labs.sdm.practica.databases.HighscoreSqlHelper;
import labs.sdm.practica.pojo.HighScore;
import labs.sdm.practica.pojo.HighScoreList;

public class ScoresActivity extends AppCompatActivity {

    SharedPreferences prefs;

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

    TabHost host;

    GetScoresAsyncTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        context = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        scoresLocalListView = (ListView) findViewById(R.id.lvLocal);
        scoresFriendsListView = (ListView) findViewById(R.id.lvFriends);

        hashMapLocalList = new ArrayList<>();
        hashMapFriendsList = new ArrayList<>();

        hashMapLocalList.addAll(HighscoreSqlHelper.getInstance(this).getHighscores());

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

//        hashMapFriendsList = hashMapLocalList;

        adapterLocal = new SimpleAdapter(this,hashMapLocalList,R.layout.list_view_row_scores,new String[]{"name", "score"},new int[]{R.id.tvScoresName, R.id.tvScoresScore} );
        scoresLocalListView.setAdapter(adapterLocal);






        host = (TabHost) findViewById(R.id.tabHost);
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
        //show or hide delete button
        if (hashMapLocalList.size() > 0 && (host.getCurrentTab() == 0)) {
            clearAllLocalHighscores = true;
        } else {
            clearAllLocalHighscores = false;
        }
        invalidateOptionsMenu();

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                //show or hide delete button
                if (hashMapLocalList.size() > 0 && (host.getCurrentTab() == 0)) {
                    clearAllLocalHighscores = true;
                } else{
                    clearAllLocalHighscores = false;
                }
                invalidateOptionsMenu();
                if(host.getCurrentTab() == 1){
                    getOnlineScores();
                    Log.d("ScoresActivity", "Tab1: Name: " + hashMapFriendsList.get(0).get("name") + ", Score: " + hashMapFriendsList.get(0).get("score"));
                    Log.d("ScoresActivity", "Tab1: Name: " + hashMapFriendsList.get(1).get("name") + ", Score: " + hashMapFriendsList.get(1).get("score"));

                    adapterFriends = new SimpleAdapter(context,hashMapFriendsList,R.layout.list_view_row_scores,new String[]{"name", "score"},new int[]{R.id.tvScoresName, R.id.tvScoresScore} );
                    scoresFriendsListView.setAdapter(adapterFriends);

                }
            }});



        //Get highscores
        if (isConnectionAvailable()){

            String userName = prefs.getString("userName", "anonymous");
            //new task
            task = new GetScoresAsyncTask();
            // Start the task
            task.execute(userName);

//            hashMapFriendsList = task.getHashMapList();

//            hashMapFriendsList = task.hashMapList;

        }
        // There is no Internet connection available, so inform the user about that
        else {
            Toast.makeText(this, R.string.connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void getOnlineScores(){
        //Get highscores
        if (isConnectionAvailable()){

            String userName = prefs.getString("userName", "anonymous");
            //new task
            task = new GetScoresAsyncTask();
            // Start the task
            task.execute(userName);

//            hashMapFriendsList = task.getHashMapList();

//            hashMapFriendsList = task.hashMapList;

        }
        // There is no Internet connection available, so inform the user about that
        else {
            Toast.makeText(this, R.string.connection_not_available, Toast.LENGTH_SHORT).show();
        }
//        adapterFriends.notifyDataSetChanged();
//        adapterFriends.notifyDataSetInvalidated();
//        Log.d("ScoresActivity", "GOS: Name: " + hashMapFriendsList.get(0).get("name") + ", Score: " + hashMapFriendsList.get(0).get("score"));


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



    /*
This method is executed when the activity is created to populate the ActionBar with actions
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_scores, menu);


        menu.findItem(R.id.action_delete_scores).setVisible(clearAllLocalHighscores );

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

    private class GetScoresAsyncTask extends AsyncTask<String, Void, Void > {


        public List<HashMap<String, String>> hashMapList = new ArrayList<>();
        HashMap<String,String> item = null;



        @Override
        protected Void doInBackground(String... params) {

            String userName = params[0];

            // Build the URI to access the web service at http://wwtbamandroid.appspot.com/rest/highscores
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("http");
            uriBuilder.authority("wwtbamandroid.appspot.com");
            uriBuilder.appendPath("rest");
            uriBuilder.appendPath("highscores");
            uriBuilder.appendQueryParameter("name", userName);



            HighScoreList result = null;
            try {
                // Creates a new URL from the URI
                URL url = new URL(uriBuilder.build().toString());
                // Get a connection to the web service
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                // Process the response if it was successful (HTTP_OK = 200)
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("GetScoresAsyncTask", "HTTP Response OK");
                    // Open an input channel to receive the response from the web service
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    // Create a Gson object through a GsonBuilder to process the response
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    try {
                        // Deserializes the JSON response into a HighScoreList object
                        result = gson.fromJson(reader, HighScoreList.class);
                    } catch (JsonSyntaxException | JsonIOException e) {
                        e.printStackTrace();
                    }

                    List<HighScore> lhs = result.getScores();
                    Collections.sort(lhs);

                    //iterate over all highscores and add them to the hashmap-list
                    for (int i=0; i<lhs.size();i++){
                        item = new HashMap<>();
                        HighScore highScore = lhs.get(i);
                        String name = highScore.getName();
                        String score = highScore.getScoring();
                        item.put("name",name);
                        item.put("score", score);
                        hashMapList.add(item);

                    Log.d("GetScoresAsyncTask", "Name: " + name + ", Score: " + score);


                    }

                    Log.d("GetScoresAsyncTask", gson.toJson(result));
//                Log.d("GetScoresAsyncTask", hs0.getName() + " " + hs0.getScoring() + " " + hs0.getLatitude() + " " + hs0.getLongitude());

                    // Close the input channel
                    reader.close();
                }

                // Close the connection
                connection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



//        hashMapList.get(0).get("name");
//            Log.d("GetScoresAsyncTask", "Name: " + hashMapList.get(0).get("name") + ", Score: " + hashMapList.get(0).get("score"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            hashMapFriendsList = hashMapList;
            Log.d("ScoresActivity", "Name: " + hashMapFriendsList.get(0).get("name") + ", Score: " + hashMapFriendsList.get(0).get("score"));
            Log.d("ScoresActivity", "Name: " + hashMapFriendsList.get(1).get("name") + ", Score: " + hashMapFriendsList.get(1).get("score"));

//            adapterFriends.notifyDataSetChanged();
//            adapterFriends.notifyDataSetInvalidated();
        }
    }



}
