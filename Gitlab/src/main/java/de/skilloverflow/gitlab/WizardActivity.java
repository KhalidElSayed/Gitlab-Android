package de.skilloverflow.gitlab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.skilloverflow.gitlab.fragments.SignIn;
import de.skilloverflow.gitlab.fragments.WelcomeScreen;
import de.skilloverflow.gitlab.utils.Utils;

public class WizardActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmenthost);

        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int startId = intent.getExtras().getInt(Utils.INTENT_START_ID);

        switch (startId) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_content_container, WelcomeScreen.newInstance())
                        .commit();
                break;

            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_content_container, SignIn.newInstance())
                        .commit();
                break;

            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_content_container, WelcomeScreen.newInstance())
                        .commit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // Workaround for Crouton issue #24 (https://github.com/keyboardsurfer/Crouton/issues/24).
        Crouton.clearCroutonsForActivity(this);
        super.onDestroy();
    }
}
