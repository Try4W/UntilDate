package org.flycraft.android.untildate.activities;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.flycraft.android.untildate.BuildConfig;
import org.flycraft.android.untildate.NoteColors;
import org.flycraft.android.untildate.data.NotesStorage;
import org.flycraft.android.untildate.dialogs.InfoDialogFragment;
import org.flycraft.android.untildate.fragments.NotesListFragment;
import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.utils.OsUtils;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static boolean DEBUG_MODE = BuildConfig.DEBUG;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotesStorage.init(this);
        NoteColors.init(this);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) {
            throw new NullPointerException("getSupportActionBar() is null");
        }

        initAppRate();
        openDateListFragment();
    }

    private void initAppRate() {
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(5) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
    }


    private void openDateListFragment() {
        putFragment(new NotesListFragment(), getString(R.string.dates_list_fragment_title));
    }

    private void putFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, fragment);
        transaction.commit();

        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate_app:
                OsUtils.openAppInGooglePlay(this);
                return true;
            case R.id.info:
                openInfoDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openInfoDialog() {
        DialogFragment dialog = new InfoDialogFragment();
        dialog.show(getSupportFragmentManager(), "InfoDialogFragment_tag");
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public static boolean isDebugMode() {
        return DEBUG_MODE;
    }

}
