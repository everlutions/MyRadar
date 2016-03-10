package nl.everlutions.myradar;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import io.fabric.sdk.android.Fabric;

public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    public static final String TAG = BaseApplication.class.getSimpleName();
    private static Context appContext;
    public static Context getAppContext()
    {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appContext = getApplicationContext();

        registerActivityLifecycleCallbacks(this);
        ScreenOffReceiver screenOffReceiver = new ScreenOffReceiver();
        registerReceiver(screenOffReceiver, new IntentFilter(
                "android.intent.action.SCREEN_OFF"));

        initImageLoader();


    }

    private void initImageLoader() {

        DisplayImageOptions options = getDefaultDisplayImageOptions().build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(4)
//                 For HTTPS support
//                .imageDownloader(new SecureImageDownloader(this))
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions.Builder getDefaultDisplayImageOptions() {
        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true);
        return options;
    }

    public static boolean wasInBackground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle arg1) {
        wasInBackground = false;
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle arg1) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        wasInBackground = false;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            wasInBackground = true;
        }
    }

    class ScreenOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wasInBackground = true;
        }
    }
}
