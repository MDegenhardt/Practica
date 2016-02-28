package labs.sdm.practica.tasks;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class AddFriendAsyncTask extends AsyncTask<String, Void, Void > {

    @Override
    protected Void doInBackground(String... params) {

        String userName = params[0];
        String friendName = params[1];

        // Used to store the body
        String body = null;

        // Build the URI to access the web service at http://wwtbamandroid.appspot.com/rest/friends
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http");
        uriBuilder.authority("wwtbamandroid.appspot.com");
        uriBuilder.appendPath("rest");
        uriBuilder.appendPath("friends");

        try {
            body = "name="
                    + URLEncoder.encode(userName, "UTF-8")
                    + "&friend_name="
                    + URLEncoder.encode(friendName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            // Creates a new URL from the URI
            URL url = new URL(uriBuilder.build().toString());
            // Get a connection to the web service
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Check if the response was successful (HTTP_OK = 200)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("AddFriendAsyncTask", "HTTP Response OK");
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


        return null;
    }
}
