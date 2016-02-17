package labs.sdm.practica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
