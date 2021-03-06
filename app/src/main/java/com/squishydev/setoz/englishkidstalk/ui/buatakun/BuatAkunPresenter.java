package com.squishydev.setoz.englishkidstalk.ui.buatakun;

import android.util.Log;

import com.androidnetworking.error.ANError;
import com.squishydev.setoz.englishkidstalk.data.DataManager;
import com.squishydev.setoz.englishkidstalk.data.network.model.User;
import com.squishydev.setoz.englishkidstalk.data.network.model.UserResponse;
import com.squishydev.setoz.englishkidstalk.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BuatAkunPresenter<V extends BuatAkunMvpView> extends BasePresenter<V>
        implements BuatAkunMvpPresenter<V> {

    private static final String TAG = "BuatAkunPresenter";

    @Inject
    public BuatAkunPresenter(DataManager dataManager,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        getMvpView().setNama(getDataManager().getPrefName());
    }

    @Override
    public void registerUser(String name, String password) {
        getMvpView().showLoading();
        String nickName = getDataManager().getPrefName();
        int gender = getDataManager().getAvatarType();
        getCompositeDisposable().add(getDataManager().registerUser(nickName,name,password,gender,0,0)
                .flatMap(user -> {
                    Log.d(TAG, "registerUser: regsiter su,ses" + user.toString());
                    getDataManager().setAvatarType(user.getGender());
                    getDataManager().setUserId(String.valueOf(user.getId()));
                    getDataManager().setInventoryId(String.valueOf(user.getInventoryId()));
                    return getDataManager().loginUser(user.getUsername(),user.getPassword());
                })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                tokenResponse -> {
                    getMvpView().hideLoading();
                    Log.d(TAG, "registerUser: loginsukses" + tokenResponse.toString());
                    getDataManager().setLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER_LOGIN);
                    getDataManager().setToken(tokenResponse.getToken());
                    getMvpView().openDashboardActivity();
                },throwable ->  {
                    baseHandleError(throwable);
                    getMvpView().hideLoading();
                }
        ));
    }

    @Override
    public String getNama() {
        return getDataManager().getPrefName();
    }
}
