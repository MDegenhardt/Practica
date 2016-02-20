package labs.sdm.practica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class ScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec(("TAB1"));

        //Tab indicator specified as Label and Icon

        spec.setIndicator("Label1",getResources().getDrawable(R.mipmap.ic_launcher));

        spec.setContent(R.id.linearLayout);

        host.addTab(spec);

        spec = host.newTabSpec(("TAB2"));

        //Tab indicator specified as Label and Icon

        spec.setIndicator("Label2",getResources().getDrawable(R.mipmap.ic_launcher));

        spec.setContent(R.id.linearLayout2);

        host.addTab(spec);


        host.setCurrentTab(1);
    }
}
