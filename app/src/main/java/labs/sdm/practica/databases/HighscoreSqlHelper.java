package labs.sdm.practica.databases;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class HighscoreSqlHelper extends SQLiteOpenHelper{

    // Singleton pattern to centralize access to the database
    private static HighscoreSqlHelper instance;

    public synchronized static HighscoreSqlHelper getInstance(Context context) {
        if (instance == null) {
            instance = new HighscoreSqlHelper(context.getApplicationContext());
        }
        return instance;
    }

    /*
    Create a helper object to manage a database
*/
    private HighscoreSqlHelper(Context context) {
        // Parameters:
        //  context
        //  filename of the database, or null for in-memory database
        //  factory to create cursor objects, default if null
        //  version of the database (upgrades/downgrades existing ones)
        super(context, "highscore_database", null, 1);
    }

    /*
        This method is only called to create the database the first time it is accessed
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create a HighscoreTable table with
        //  autoincremental integer primary key: id
        //  String unique: name
        //  int: score
        db.execSQL("CREATE TABLE HighscoresTable " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, score INTEGER NOT NULL);");
    }

    /*
    This is method is only called when the database needs to be upgraded,
    so it has been left blank
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
    Insert a new highscore into the database
    */
    public void addHighscore(String name, int score) {

        // Get access to the database in write mode
        SQLiteDatabase database = getWritableDatabase();
        // Insert the new highscore into the table (autoincremental id)
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("score", score);
        database.insert("HighscoresTable", null, values);
        // Close the database helper
        database.close();
    }

    /*
    Get ArrayList<HashMap<String,String>> object with all the highscores stored
    in the database to generate the data source to be later linked to a ListView:
*/
    public ArrayList<HashMap<String, String>> getHighscores() {

        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        HashMap<String, String> item;

        // Get access to the database in read mode
        SQLiteDatabase database = getReadableDatabase();
        // Query the table to get the name and score for all existing entries in descending order
        Cursor cursor = database.query(
                "HighscoresTable", new String[]{"name", "score"}, null, null, null,null, "score DESC");
        // Go through the resulting cursor
        while (cursor.moveToNext()) {
            // Create a HashMap<String, String> object for the given entry in the database
            //  quotation text under the key "name"
            //  quotation author under the key "score"
            item = new HashMap<>();
            item.put("name", cursor.getString(0));
            item.put("score", Integer.toString(cursor.getInt(1)));
            // Add the object to the result list
            result.add(item);
        }
        // Close cursor and database helper
        cursor.close();
        database.close();

        return result;
    }

    /*
    Remove all entries from the database
*/
    public void clearAllHighscores() {

        // Get access to the database in write mode
        SQLiteDatabase database = getWritableDatabase();
        // Delete all entries from the table
        database.delete("HighscoresTable", null, null);
        // Close the database helper
        database.close();
    }
}
