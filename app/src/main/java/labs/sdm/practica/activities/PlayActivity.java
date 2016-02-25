package labs.sdm.practica.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import labs.sdm.practica.R;
import labs.sdm.practica.pojo.Question;

public class PlayActivity extends AppCompatActivity {

    SharedPreferences prefs;
    List<Question> questions;

    private int[] mMoneyValues = {0,100,200,300,500,1000,2000,4000,8000,16000,32000,64000,125000,250000,500000,100000};

    private int mCurrentQuestion; //1-15
    private int mAchievedPoints;
    private int mJokerMode;

    private boolean mFiftyAvailable, mPhoneAvailable, mAudienceAvailable, mJokersSet;


    private Menu optionsMenu;

    TextView tvCurrentMoney;
    TextView tvCurrentQuestion;

    TextView tvQuestionText;

    Button bAnswer1, bAnswer2, bAnswer3, bAnswer4;


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

//        int question = prefs.getInt("currentQuestion", 1);
        mAchievedPoints = prefs.getInt("achievedPoints", 0);
        mCurrentQuestion = prefs.getInt("currentQuestion", 1);
        mJokerMode = prefs.getInt("helpNumber",0);

        questions = generateQuestionList();

        updateDisplay(mCurrentQuestion);

    }

    //updates the displayed infos regarding the passed question number (1-15)
    @SuppressLint("StringFormatInvalid")
    private void updateDisplay(int currentQuestion){
        if(currentQuestion < 1 || currentQuestion > 15){
            Log.e("PlayActivity", "INVALID QUESTION NUMBER: " + currentQuestion);
            return;
        }
        tvCurrentQuestion.setText(String.format(getResources().getString(R.string.QuestionNumber),
                mCurrentQuestion));

        tvCurrentMoney.setText(String.format(getResources().getString(R.string.PlayForAmount),
                mMoneyValues[mCurrentQuestion]));

        Question q = questions.get(currentQuestion-1);

        Log.d("PlayActivity",q.getText());

        bAnswer1.setEnabled(true);
        bAnswer1.setAlpha(1.0f);
        bAnswer2.setEnabled(true);
        bAnswer2.setAlpha(1.0f);
        bAnswer3.setEnabled(true);
        bAnswer3.setAlpha(1.0f);
        bAnswer4.setEnabled(true);
        bAnswer4.setAlpha(1.0f);


        tvQuestionText.setText( q.getText());
        bAnswer1.setText(q.getAnswer1());
        bAnswer2.setText(q.getAnswer2());
        bAnswer3.setText(q.getAnswer3());
        bAnswer4.setText(q.getAnswer4());

        return;
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

        if (mCurrentQuestion > 1){
            editor.putBoolean("bResume", true);
        } else {
            editor.putBoolean("bResume", false);
        }
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
        Log.d("PlayActivity", "Correct answer: " + correctAnswer);

        switch(view.getId()){
            case R.id.bAnswer1 :
                answerSelected = 1;
                Log.d("PlayActivity", "Answer 1 selected");
                break;
            case R.id.bAnswer2 :
                answerSelected = 2;
                Log.d("PlayActivity", "Answer 2 selected");
                break;
            case R.id.bAnswer3 :
                answerSelected = 3;
                Log.d("PlayActivity", "Answer 3 selected");
                break;
            case R.id.bAnswer4 :
                answerSelected = 4;
                Log.d("PlayActivity", "Answer 4 selected");
                break;

        }

        if (answerSelected == correctAnswer){
            mCurrentQuestion++;
            mAchievedPoints = mMoneyValues[mCurrentQuestion];
            updateDisplay(mCurrentQuestion);
        } else{ //lost...

            //reached at least level 5
            if (mCurrentQuestion>5 && mCurrentQuestion<10){
                Log.d("PlayActivity", "Won " + mMoneyValues[5] + "€");
                mAchievedPoints = mMoneyValues[5];
            }
            //reached at least level 10
            else if(mCurrentQuestion>=10 && mCurrentQuestion < 15){
                Log.d("PlayActivity", "Won " + mMoneyValues[10] + "€");
                mAchievedPoints = mMoneyValues[10];
            }
            else{
                Log.d("PlayActivity", "Won 0€");
                mAchievedPoints = 0;
            }
//            mCurrentQuestion = 1;
//            startActivity(new Intent(this,MainActivity.class));
            finishedGame();
        }

    }

    public void finishedGame(){

        Toast.makeText(this, String.format(getResources().getString(R.string.endMessage),
                mAchievedPoints), Toast.LENGTH_LONG).show();

        //TODO: insert highscores
        //reset for next game
        mCurrentQuestion = 1;
        mAchievedPoints = 0;
        mJokersSet = false;
        startActivity(new Intent(this,MainActivity.class));


    }

    /*
                This method is executed when the activity is created to populate the ActionBar with actions
                */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        optionsMenu = menu;

        getMenuInflater().inflate(R.menu.menu_play, menu);
//        if (mJokerMode == 0){
//            menu.findItem(R.id.action_joker_fifty).setVisible(false);
//            menu.findItem(R.id.action_joker_telephone).setVisible(false);
//            menu.findItem(R.id.action_joker_audience).setVisible(false);
//        } else if (mJokerMode == 1){
//            menu.findItem(R.id.action_joker_fifty).setVisible(true);
//            menu.findItem(R.id.action_joker_telephone).setVisible(false);
//            menu.findItem(R.id.action_joker_audience).setVisible(false);
//        } else if (mJokerMode == 2){
//            menu.findItem(R.id.action_joker_fifty).setVisible(true);
//            menu.findItem(R.id.action_joker_telephone).setVisible(true);
//            menu.findItem(R.id.action_joker_audience).setVisible(false);
//        } else if (mJokerMode == 3){
//            menu.findItem(R.id.action_joker_fifty).setVisible(true);
//            menu.findItem(R.id.action_joker_telephone).setVisible(true);
//            menu.findItem(R.id.action_joker_audience).setVisible(true);
//        }


        menu.findItem(R.id.action_joker_fifty).setVisible(mFiftyAvailable);
        menu.findItem(R.id.action_joker_telephone).setVisible(mPhoneAvailable);
        menu.findItem(R.id.action_joker_audience).setVisible(mAudienceAvailable);
        menu.findItem(R.id.action_quit).setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_joker_telephone:
                Log.d("PlayActivity", "Phone Joker selected");
                //updateDisplay(mCurrentQuestion);
                mPhoneAvailable = false;
                //optionsMenu.findItem(R.id.action_joker_telephone).setVisible(false);
                invalidateOptionsMenu();

                return true;

            case R.id.action_joker_fifty:
                Log.d("PlayActivity", "50-50 Joker selected");

                //updateDisplay(mCurrentQuestion);
                //optionsMenu.findItem(R.id.action_joker_fifty).setVisible(false);
                mFiftyAvailable = false;
                invalidateOptionsMenu();

                //eliminate 2 wrong answers
                Question q = questions.get(mCurrentQuestion-1);
                int f1 = Integer.parseInt(q.getFifty1());
                int f2 = Integer.parseInt(q.getFifty2());

                switch (f1){
                    case 1:
                        bAnswer1.setEnabled(false);
                        //TODO create Button states
                        bAnswer1.setAlpha(.5f);
                        break;
                    case 2:
                        bAnswer2.setEnabled(false);
                        //TODO create Button states
                        bAnswer2.setAlpha(.5f);
                        break;
                    case 3:
                        bAnswer3.setEnabled(false);
                        //TODO create Button states
                        bAnswer3.setAlpha(.5f);
                        break;
                    case 4:
                        bAnswer4.setEnabled(false);
                        //TODO create Button states
                        bAnswer4.setAlpha(.5f);
                        break;
                    default:
                        break;
                }
                switch (f2){
                    case 1:
                        bAnswer1.setEnabled(false);
                        //TODO create Button states
                        bAnswer1.setAlpha(.5f);
                        break;
                    case 2:
                        bAnswer2.setEnabled(false);
                        //TODO create Button states
                        bAnswer2.setAlpha(.5f);
                        break;
                    case 3:
                        bAnswer3.setEnabled(false);
                        //TODO create Button states
                        bAnswer3.setAlpha(.5f);
                        break;
                    case 4:
                        bAnswer4.setEnabled(false);
                        //TODO create Button states
                        bAnswer4.setAlpha(.5f);
                        break;
                    default:
                        break;
                }

                return true;

            case R.id.action_joker_audience:
                Log.d("PlayActivity", "Audience Joker selected");
                //updateDisplay(mCurrentQuestion);
                //optionsMenu.findItem(R.id.action_joker_audience).setVisible(false);
                mAudienceAvailable = false;
                invalidateOptionsMenu();

                return true;

            case R.id.action_quit:

//                mAchievedPoints = mMoneyValues[mCurrentQuestion];
//                Toast.makeText(this, String.format(getResources().getString(R.string.endMessage),
//                        mAchievedPoints), Toast.LENGTH_LONG).show();


                 finishedGame();

                Log.d("PlayActivity", "Quit selected");
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    public List<Question> generateQuestionList() {
        List<Question> list = new ArrayList<Question>();
        Question q = null;

        q = new Question(
                "1",
                "Which is the Sunshine State of the US?",
                "North Carolina",
                "Florida",
                "Texas",
                "Arizona",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "2",
                "Which of these is not a U.S. state?",
                "New Hampshire",
                "Washington",
                "Wyoming",
                "Manitoba",
                "4",
                "4",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "3",
                "What is Book 3 in the Pokemon book series?",
                "Charizard",
                "Island of the Giant Pokemon",
                "Attack of the Prehistoric Pokemon",
                "I Choose You!",
                "3",
                "2",
                "3",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "4",
                "Who was forced to sign the Magna Carta?",
                "King John",
                "King Henry VIII",
                "King Richard the Lion-Hearted",
                "King George III",
                "1",
                "3",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "5",
                "Which ship was sunk in 1912 on its first voyage, although people said it would never sink?",
                "Monitor",
                "Royal Caribean",
                "Queen Elizabeth",
                "Titanic",
                "4",
                "4",
                "4",
                "1",
                "2"
        );
        list.add(q);

        q = new Question(
                "6",
                "Who was the third James Bond actor in the MGM films? (Do not include &apos;Casino Royale&apos;.)",
                "Roger Moore",
                "Pierce Brosnan",
                "Timothy Dalton",
                "Sean Connery",
                "1",
                "3",
                "3",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "7",
                "Which is the largest toothed whale?",
                "Humpback Whale",
                "Blue Whale",
                "Killer Whale",
                "Sperm Whale",
                "4",
                "2",
                "2",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "8",
                "In what year was George Washington born?",
                "1728",
                "1732",
                "1713",
                "1776",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "9",
                "Which of these rooms is in the second floor of the White House?",
                "Red Room",
                "China Room",
                "State Dining Room",
                "East Room",
                "2",
                "2",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "10",
                "Which Pope began his reign in 963?",
                "Innocent III",
                "Leo VIII",
                "Gregory VII",
                "Gregory I",
                "2",
                "1",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "11",
                "What is the second longest river in South America?",
                "Parana River",
                "Xingu River",
                "Amazon River",
                "Rio Orinoco",
                "1",
                "1",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "12",
                "What Ford replaced the Model T?",
                "Model U",
                "Model A",
                "Edsel",
                "Mustang",
                "2",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "13",
                "When was the first picture taken?",
                "1860",
                "1793",
                "1912",
                "1826",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "14",
                "Where were the first Winter Olympics held?",
                "St. Moritz, Switzerland",
                "Stockholm, Sweden",
                "Oslo, Norway",
                "Chamonix, France",
                "4",
                "1",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "15",
                "Which of these is not the name of a New York tunnel?",
                "Brooklyn-Battery",
                "Lincoln",
                "Queens Midtown",
                "Manhattan",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        return list;
    }

}
