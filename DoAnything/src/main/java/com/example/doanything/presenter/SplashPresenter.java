
package com.example.doanything.presenter;

import com.example.doanything.view.ISplashView;

public class SplashPresenter {

    private ISplashView mView;

    public SplashPresenter() {

    }

    public void setView(ISplashView mISplashView) {
        // TODO Auto-generated method stub
        mView = mISplashView;
    }

    public void didFinishLoading() {
        // TODO Auto-generated method stub
        ISplashView view = getView();

        // if (mConnectionStatus.isOnline()) {
        view.moveToSelectView();
        // } else {
        // view.hideProgress();
        // view.showNoInetErrorMsg();
        // }
    }

    private ISplashView getView() {
        // TODO Auto-generated method stub
        return mView;
    }

}
