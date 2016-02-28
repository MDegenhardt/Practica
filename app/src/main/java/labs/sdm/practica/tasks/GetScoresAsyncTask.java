package labs.sdm.practica.tasks;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.List;

import labs.sdm.practica.pojo.HighScore;
import labs.sdm.practica.pojo.HighScoreList;

public class GetScoresAsyncTask extends AsyncTask<String, Void, Void > {

    public List<HashMap<String, String>> getHashMapList() {
        return hashMapList;
    }

    public List<HashMap<String, String>> hashMapList = new ArrayList<>();
    HashMap<String,String> item = new HashMap<>();



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

                //iterate over all highscores and add them to the hashmap-list
                for (int i=0; i<lhs.size();i++){
                    HighScore highScore = lhs.get(i);
                    String name = highScore.getName();
                    String score = highScore.getScoring();

                    item.put("name",name);
                    item.put("score", score);
                    hashMapList.add(item);

//                    Log.d("GetScoresAsyncTask", "Name: " + name + ", Score: " + score);


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
        Log.d("GetScoresAsyncTask", "Name: " + hashMapList.get(0).get("name") + ", Score: " + hashMapList.get(0).get("score"));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }
}
