package de.skilloverflow.gitlab;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.skilloverflow.gitlab.utils.App;
import de.skilloverflow.gitlab.utils.Utils;
import de.skilloverflow.gitlab.utils.misc.Interfaces;

public class MainActivity extends FragmentActivity {

    protected static ToggleListener sToggleListener;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

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

        ActionBar actionBar = getActionBar();
        sToggleListener = new ToggleListener();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
                DrawerNavigation.sNavigationTransactionListener.onDrawerClosed();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                DrawerNavigation.sNavigationTransactionListener.onDrawerOpened();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new DrawerNavigation())
                .commit();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // Workaround for Crouton issue #24 (https://github.com/keyboardsurfer/Crouton/issues/24).
        Crouton.clearCroutonsForActivity(this);
        super.onDestroy();
    }

    public final class ToggleListener implements Interfaces.SlidingMenuListener {

        @Override
        public void onShowAbove() {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerToggle.syncState();
        }
    }
}
