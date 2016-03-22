package org.flycraft.android.untildate.ui.activities.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class MainActivityPresenter extends MvpBasePresenter<MainActivityView> {

    public void initialize() {
        if(isViewAttached()) {
            getView().showAppRateDialogIfMeetsConditions();
            getView().openDateListFragment();
        }
    }

    public void showAppInfoDialog() {
        if(isViewAttached()) {
            getView().showAppInfoDialog();
        }
    }

}
