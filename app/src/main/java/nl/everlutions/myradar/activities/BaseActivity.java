package nl.everlutions.myradar.activities;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;

import nl.everlutions.myradar.R;
import nl.everlutions.myradar.comm.ApiManager;
import nl.everlutions.myradar.comm.CustomSpiceService;
import nl.everlutions.myradar.gcm.GcmRegistrationIntentService;
import nl.everlutions.myradar.utils.Constants;
import nl.everlutions.myradar.utils.DialogUtil;
import nl.everlutions.myradar.utils.PrefsUtil;

public abstract class BaseActivity extends AppCompatActivity implements Constants {
    public final String TAG = this.getClass().getSimpleName();

    private Toolbar mActionBarToolbar;
    private Dialog mLoadingDialog;
    private SpiceManager spiceManager = new SpiceManager(CustomSpiceService.class);
    public SpiceRequest mCurrentTask;
    public ApiManager mAPI;
    private TextView mMemoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * This part of the code makes sure the app doens't get restarted if it crashes.
         */
        if (savedInstanceState != null && savedInstanceState.getInt("SavedInstance") > 0) {
            // ----- Your prefferred way to kill an application ----
            try {
                this.finishActivity(0);
            } catch (Exception ee) {
            }
            try {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            } catch (Exception eeee) {
            }
            return;
        }
        super.onCreate(savedInstanceState);
        mAPI = new ApiManager(this, getSpiceManager());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionToolbar();
    }

    protected Toolbar getActionToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SavedInstance", 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG_MODE) {
            mMemoryRefreshTimer.start();
        }
    }

    private void addMemoryView() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        addContentView(mMemoryView, params);
    }

    CountDownTimer mMemoryRefreshTimer = new CountDownTimer(1000, 500) {
        @Override
        public void onTick(long millisUntilFinished) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            int memoryClass = am.getMemoryClass();
            final Runtime runtime = Runtime.getRuntime();
            final long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
            mMemoryView.setText(usedMemInMB + "MB of " + memoryClass + "MB");
        }

        @Override
        public void onFinish() {
            mMemoryRefreshTimer.start();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (DEBUG_MODE) {
            mMemoryRefreshTimer.cancel();
        }
        mAPI.cancelCurrentTasks();
        hideLoader();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }


    /**
     * For DEBUG Pruposes a memory view is added so that it can be checked how much memory the app uses
     * <p/>
     * Using the Applicaiton class to determine if the user has been away from the app. When the
     * user comes back to the app the token is checked for validity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG_MODE) {
            mMemoryView = new TextView(this);
            mMemoryView.setBackgroundColor(Color.BLACK);
            mMemoryView.setTextColor(Color.WHITE);
            addMemoryView();
        }
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    public synchronized void showLoader(DialogInterface.OnCancelListener onCancelListener) {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtil.getLoadingDialog(this);
            if (onCancelListener != null) {
                mLoadingDialog.setOnCancelListener(onCancelListener);
            }
            mLoadingDialog.show();
        }
    }

    public synchronized void hideLoader() {
        if (mLoadingDialog == null) {
            return;
        }
        mLoadingDialog.dismiss();
        mLoadingDialog = null;
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public ApiManager getAPI() {
        return mAPI;
    }

    private void registerToGCM() {
        if (checkPlayServices()&& !PrefsUtil.readBool(this, PREF_KEY_GCM_SUBSCRIBING_SUCCESSFUL, false)) {
            Intent intent = new Intent(this, GcmRegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                DialogUtil.showMsg(this, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
