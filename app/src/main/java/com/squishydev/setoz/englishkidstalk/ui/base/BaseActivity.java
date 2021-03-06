package com.squishydev.setoz.englishkidstalk.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squishydev.setoz.englishkidstalk.App;
import com.squishydev.setoz.englishkidstalk.R;
import com.squishydev.setoz.englishkidstalk.di.componen.ActivityComponent;
import com.squishydev.setoz.englishkidstalk.di.componen.DaggerActivityComponent;
import com.squishydev.setoz.englishkidstalk.di.module.ActivityModule;
import com.squishydev.setoz.englishkidstalk.utils.CommonUtils;
import com.squishydev.setoz.englishkidstalk.utils.MediaUtils;
import com.squishydev.setoz.englishkidstalk.utils.NetworkUtils;

import cn.refactor.lib.colordialog.PromptDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by miftahun on 9/21/18.
 * Email : miftahunajat@gmail.com
 * Github : https://github.com/miftahunajat/
 */

public abstract class BaseActivity extends AppCompatActivity
        implements MvpView, BaseFragment.Callback {

    private ProgressDialog mProgressDialog;

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((App) getApplication()).getComponent())
                .build();
        setUp();

    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showSuccessPrompt() {
        MediaUtils.playSound(this,R.raw.success_sound);
        new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.success))
                .setContentText(getString(R.string.text))
                .setPositiveListener(getString(R.string.ok), PromptDialog::dismiss).show();
    }

    @Override
    public void showFailedPrompt() {
        MediaUtils.playSound(this,R.raw.lose);
        new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.wrong))
                .setContentText(getString(R.string.text_wrong))
                .setPositiveListener(getString(R.string.ok),PromptDialog::dismiss).show();
    }

    public void showSuccessPrompt(String title, String content, PromptDialog.OnPositiveListener listener) {
        MediaUtils.playSound(this,R.raw.success_sound);
        new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(content)
                .setPositiveListener(getString(R.string.ok), listener).show();
    }

    public void showFailedPrompt(String title, String content, PromptDialog.OnPositiveListener listener) {
        MediaUtils.playSound(this,R.raw.lose);
        new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(content)
                .setPositiveListener(getString(R.string.ok),listener).show();
    }

    protected abstract void setUp();
}