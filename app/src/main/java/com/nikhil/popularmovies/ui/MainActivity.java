package com.nikhil.popularmovies.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nikhil.popularmovies.R;

public class MainActivity extends AppCompatActivity {


    private FrameLayout mainContainer;
    private boolean mTwoPane = false;
    private MoviesListFragment moviesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detailContainer) !=null) {
            mTwoPane = true;
        }
        mainContainer = (FrameLayout) findViewById(R.id.mainContainer);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, getFragment(), MoviesListFragment.class.getSimpleName()).commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            if (mTwoPane) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(DetailFragment.class.getSimpleName());
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new SettingsFragment(), SettingsFragment.class.getSimpleName()).commitAllowingStateLoss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.mainContainer);
    }

    @Override
    public void onBackPressed() {

        if (getCurrentFragment() instanceof SettingsFragment || getCurrentFragment() instanceof DetailFragment) {
            getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, getFragment(), MoviesListFragment.class.getSimpleName()).commitAllowingStateLoss();
        } else {
            super.onBackPressed();
        }
    }

    private MoviesListFragment getFragment() {


        moviesListFragment = new MoviesListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTwoPane", mTwoPane);
        moviesListFragment.setArguments(bundle);
        return moviesListFragment;
    }

}
