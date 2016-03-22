package org.flycraft.android.untildate.ui.activities.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface MainActivityView extends MvpView {

    void showAppInfoDialog();
    void openDateListFragment();
    void showAppRateDialogIfMeetsConditions();

}
