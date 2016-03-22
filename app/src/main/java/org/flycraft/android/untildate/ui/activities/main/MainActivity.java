package org.flycraft.android.untildate.ui.activities.main;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import org.flycraft.android.untildate.BuildConfig;
import org.flycraft.android.untildate.NoteColors;
import org.flycraft.android.untildate.data.NotesStorage;
import org.flycraft.android.untildate.dialogs.InfoDialogFragment;
import org.flycraft.android.untildate.ui.fragments.NotesListFragment;
import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.utils.OsUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity
        extends MvpActivity<MainActivityView, MainActivityPresenter>
        implements MainActivityView {

    private static final String TAG = "MainActivity";
    private static boolean DEBUG_MODE = BuildConfig.DEBUG;

    @Bind(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fab) FloatingActionButton fab;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotesStorage.init(this);
        NoteColors.init(this);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) {
            throw new NullPointerException("getSupportActionBar() is null");
        }
        presenter.initialize();
    }

    @NonNull
    @Override
    public MainActivityPresenter createPresenter() {
        return new MainActivityPresenter();
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
                presenter.showAppInfoDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openDateListFragment() {
        putFragment(new NotesListFragment(), getString(R.string.dates_list_fragment_title));
    }

    @Override
    public void showAppRateDialogIfMeetsConditions() {
        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @Override
    public void showAppInfoDialog() {
        DialogFragment dialog = new InfoDialogFragment();
        dialog.show(getSupportFragmentManager(), "InfoDialogFragment_tag");
    }

    private void putFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, fragment);
        transaction.commit();

        actionBar.setTitle(title);
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
