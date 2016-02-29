package labs.sdm.practica.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import labs.sdm.practica.R;
import labs.sdm.practica.databases.HighscoreSqlHelper;
import labs.sdm.practica.parser.QuestionXmlParser;
import labs.sdm.practica.pojo.Question;
import labs.sdm.practica.tasks.PutHighscoresAsyncTask;

public class PlayActivity extends AppCompatActivity {

    SharedPreferences prefs;
    List<Question> questions;
    TextView tvCurrentMoney;
    TextView tvCurrentQuestion;
    TextView tvQuestionText;
    Button bAnswer1, bAnswer2, bAnswer3, bAnswer4;
    PutHighscoresAsyncTask task;
    private int[] mMoneyValues = {0, 100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000};
    private int mCurrentQuestion; //1-15
    private int mAchievedPoints;
    private int mJokerMode;
    private boolean mFiftyAvailable, mPhoneAvailable, mAudienceAvailable, mJokersSet;
    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        tvCurrentMoney = (TextView) findViewById(R.id.tvQuestion);
        tvCurrentQuestion = (TextView) findViewById(R.id.tvPlayForAmount);
        tvQuestionText = (TextView) findViewById(R.id.tvQuestionText);
        bAnswer1 = (Button) findViewById(R.id.bAnswer1);
        bAnswer2 = (Button) findViewById(R.id.bAnswer2);
        bAnswer3 = (Button) findViewById(R.id.bAnswer3);
        bAnswer4 = (Button) findViewById(R.id.bAnswer4);

        mAchievedPoints = prefs.getInt("achievedPoints", 0);
        mCurrentQuestion = prefs.getInt("currentQuestion", 1);
        mJokerMode = prefs.getInt("helpNumber",3);

        InputStream inputStream = null;
        try {
            inputStream = getApplicationContext().getAssets().open(getResources().getString(R.string.filenameQuestions));
        } catch (IOException e) {
            e.printStackTrace();
        }
        QuestionXmlParser parser = new QuestionXmlParser();
        try {
            questions = parser.parseQuestion(inputStream);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        //questions = generateQuestionList();

        updateDisplay(mCurrentQuestion);

    }

    //updates the displayed infos regarding the passed question number (1-15)
    @SuppressLint("StringFormatInvalid")
    private void updateDisplay(int currentQuestion){
        if(currentQuestion < 1 || currentQuestion > 15){
//            Log.e("PlayActivity", "INVALID QUESTION NUMBER: " + currentQuestion);
            return;
        }
        tvCurrentQuestion.setText(String.format(getResources().getString(R.string.QuestionNumber),
                mCurrentQuestion));

        tvCurrentMoney.setText(String.format(getResources().getString(R.string.PlayForAmount),
                mMoneyValues[mCurrentQuestion]));

        Question q = questions.get(currentQuestion-1);

//        Log.d("PlayActivity",q.getText());

        float alphaFull = 1.0f;

        bAnswer1.setEnabled(true);
        bAnswer1.setAlpha(alphaFull);
        bAnswer2.setEnabled(true);
        bAnswer2.setAlpha(alphaFull);
        bAnswer3.setEnabled(true);
        bAnswer3.setAlpha(alphaFull);
        bAnswer4.setEnabled(true);
        bAnswer4.setAlpha(alphaFull);


        tvQuestionText.setText( q.getText());
        bAnswer1.setText(q.getAnswer1());
        bAnswer2.setText(q.getAnswer2());
        bAnswer3.setText(q.getAnswer3());
        bAnswer4.setText(q.getAnswer4());

    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("currentQuestion", mCurrentQuestion);
        editor.putInt("achievedPoints", mAchievedPoints);
        editor.putBoolean("fiftyAvailable", mFiftyAvailable);
        editor.putBoolean("phoneAvailable", mPhoneAvailable);
        editor.putBoolean("audienceAvailable", mAudienceAvailable);
        editor.putBoolean("jokersSet", mJokersSet);
        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {


        mCurrentQuestion = prefs.getInt("currentQuestion", 1);
        mAchievedPoints = prefs.getInt("achievedPoints", 0);
        mFiftyAvailable = prefs.getBoolean("fiftyAvailable", false);
        mPhoneAvailable = prefs.getBoolean("phoneAvailable", false);
        mAudienceAvailable = prefs.getBoolean("audienceAvailable", false);
        mJokersSet = prefs.getBoolean("jokersSet", false);

        //if Jokers are not set yet
        if (!mJokersSet){
            if (mJokerMode == 0){
                mFiftyAvailable = false;
                mPhoneAvailable = false;
                mAudienceAvailable = false;
            } else if (mJokerMode == 1){
                mFiftyAvailable = true;
                mPhoneAvailable = false;
                mAudienceAvailable = false;
            } else if (mJokerMode == 2){
                mFiftyAvailable = true;
                mPhoneAvailable = true;
                mAudienceAvailable = false;
            } else if (mJokerMode == 3){
                mFiftyAvailable = true;
                mPhoneAvailable = true;
                mAudienceAvailable = true;
            }
            mJokersSet = true;
        }



        super.onResume();
    }

    public void answerButtonClicked(View view){

        int answerSelected = 0;

        Question q = questions.get(mCurrentQuestion-1);
        int correctAnswer = Integer.parseInt(q.getRight());
//        Log.d("PlayActivity", "Correct answer: " + correctAnswer);

        switch(view.getId()){
            case R.id.bAnswer1 :
                answerSelected = 1;
//                Log.d("PlayActivity", "Answer 1 selected");
                break;
            case R.id.bAnswer2 :
                answerSelected = 2;
//                Log.d("PlayActivity", "Answer 2 selected");
                break;
            case R.id.bAnswer3 :
                answerSelected = 3;
//                Log.d("PlayActivity", "Answer 3 selected");
                break;
            case R.id.bAnswer4 :
                answerSelected = 4;
//                Log.d("PlayActivity", "Answer 4 selected");
                break;

        }

        if (answerSelected == correctAnswer){
            mAchievedPoints = mMoneyValues[mCurrentQuestion];
            mCurrentQuestion++;
            updateDisplay(mCurrentQuestion);

            //all 15 correct
            if(mCurrentQuestion>15){
                finishedGame(2);
            } else {
                Toast.makeText(this, String.format(getResources().getString(R.string.correctAnswerMsg),
                        mAchievedPoints), Toast.LENGTH_SHORT).show();
            }

        } else{ //lost...

            //reached at least level 5
            if (mCurrentQuestion>5 && mCurrentQuestion<10){
//                Log.d("PlayActivity", "Won " + mMoneyValues[5] + "€");
                mAchievedPoints = mMoneyValues[5];
            }
            //reached at least level 10
            else if(mCurrentQuestion>=10 && mCurrentQuestion < 15){
//                Log.d("PlayActivity", "Won " + mMoneyValues[10] + "€");
                mAchievedPoints = mMoneyValues[10];
            }
            else{
//                Log.d("PlayActivity", "Won 0€");
                mAchievedPoints = 0;
            }

            finishedGame(1);
        }

    }

    // code: 0 = user quit, 1 = lost, 2 = 15 right
    public void finishedGame(int code){

        if (code == 0){
            Toast.makeText(this, String.format(getResources().getString(R.string.endMessageQuit),
                    mAchievedPoints), Toast.LENGTH_LONG).show();
        } else if(code == 1){
            Toast.makeText(this, String.format(getResources().getString(R.string.endMessageLost),
                    mAchievedPoints), Toast.LENGTH_LONG).show();
        } else if(code == 2){
            Toast.makeText(this, String.format(getResources().getString(R.string.endMessageWon),
                    mAchievedPoints), Toast.LENGTH_LONG).show();
        }

        //Add highscores to SQL
        HighscoreSqlHelper.getInstance(this).addHighscore((prefs.getString("userName", getResources().getString(R.string.anonymousUser))), mAchievedPoints);

        //Add highscore to web
        if (isConnectionAvailable()){

            String userName = prefs.getString("userName", getResources().getString(R.string.anonymousUser));
            //new task...
            task = new PutHighscoresAsyncTask();
            // Start the task
            task.execute(userName,Integer.toString(mAchievedPoints));
        }
        // There is no Internet connection available, so inform the user about that
        else {
            Toast.makeText(this, R.string.connection_not_available, Toast.LENGTH_SHORT).show();
        }

        //reset for next game
        mCurrentQuestion = 1;
        mAchievedPoints = 0;
        mJokersSet = false;
        startActivity(new Intent(this,MainActivity.class));


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
        
        optionsMenu = menu;

        getMenuInflater().inflate(R.menu.menu_play, menu);


        menu.findItem(R.id.action_joker_fifty).setVisible(mFiftyAvailable);
        menu.findItem(R.id.action_joker_telephone).setVisible(mPhoneAvailable);
        menu.findItem(R.id.action_joker_audience).setVisible(mAudienceAvailable);
        menu.findItem(R.id.action_quit).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Question q = questions.get(mCurrentQuestion-1);
        switch (item.getItemId()) {
            case R.id.action_joker_telephone:
//                Log.d("PlayActivity", "Phone Joker selected");
                mPhoneAvailable = false;
                invalidateOptionsMenu();
                //show Toast
                Toast.makeText(this, String.format(getResources().getString(R.string.phoneJokerMessage),
                        Integer.parseInt(q.getPhone())), Toast.LENGTH_LONG).show();

                return true;

            case R.id.action_joker_fifty:
//                Log.d("PlayActivity", "50-50 Joker selected");
                mFiftyAvailable = false;
                invalidateOptionsMenu();

                //eliminate 2 wrong answers
                int f1 = Integer.parseInt(q.getFifty1());
                int f2 = Integer.parseInt(q.getFifty2());

                float alphaHalf = 0.5f;

                switch (f1){
                    case 1:
                        bAnswer1.setEnabled(false);
                        bAnswer1.setAlpha(alphaHalf);
                        break;
                    case 2:
                        bAnswer2.setEnabled(false);
                        bAnswer2.setAlpha(alphaHalf);
                        break;
                    case 3:
                        bAnswer3.setEnabled(false);
                        bAnswer3.setAlpha(alphaHalf);
                        break;
                    case 4:
                        bAnswer4.setEnabled(false);
                        bAnswer4.setAlpha(alphaHalf);
                        break;
                    default:
                        break;
                }
                switch (f2){
                    case 1:
                        bAnswer1.setEnabled(false);
                        bAnswer1.setAlpha(alphaHalf);
                        break;
                    case 2:
                        bAnswer2.setEnabled(false);
                        bAnswer2.setAlpha(alphaHalf);
                        break;
                    case 3:
                        bAnswer3.setEnabled(false);
                        bAnswer3.setAlpha(alphaHalf);
                        break;
                    case 4:
                        bAnswer4.setEnabled(false);
                        bAnswer4.setAlpha(alphaHalf);
                        break;
                    default:
                        break;
                }

                return true;

            case R.id.action_joker_audience:
//                Log.d("PlayActivity", "Audience Joker selected");
                mAudienceAvailable = false;
                invalidateOptionsMenu();

                Toast.makeText(this, String.format(getResources().getString(R.string.audienceJokerMessage),
                        Integer.parseInt(q.getAudience())), Toast.LENGTH_LONG).show();

                return true;

            case R.id.action_quit:

                 finishedGame(0);

//                Log.d("PlayActivity", "Quit selected");
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
