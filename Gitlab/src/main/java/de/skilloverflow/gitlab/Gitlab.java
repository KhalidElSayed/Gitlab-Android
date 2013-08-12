package de.skilloverflow.gitlab;

import android.app.Application;
import android.os.AsyncTask;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Gitlab extends Application {
    public static String PACKAGE_NAME;

    @Override
    public void onCreate() {
        super.onCreate();

        // Constant for accessing the package name,
        // so it doesn't have to be hardcoded and produce errors if build in a different variant.
        PACKAGE_NAME = getPackageName();

        // Create default options which will be used for every
        // displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        // Create global configuration and initialize ImageLoader with this configuration.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();

        ImageLoader.getInstance().init(config);
    }
}
