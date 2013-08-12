package de.skilloverflow.gitlab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import de.skilloverflow.gitlab.utils.App;
import de.skilloverflow.gitlab.utils.Utils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        if (!App.isWizardCompleted(context)) {
            Intent i = new Intent(context, WizardActivity.class);
            i.putExtra(Utils.INTENT_START_ID, Utils.START_WELCOME_SCREEN);
            startActivity(i);
            this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
