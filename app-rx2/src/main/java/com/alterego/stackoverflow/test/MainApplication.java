package com.alterego.stackoverflow.test;

import com.alterego.stackoverflow.test.di.AndroidModule;
import com.alterego.stackoverflow.test.di.AppComponent;
import com.alterego.stackoverflow.test.di.DaggerAppComponent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

import lombok.Getter;
import lombok.experimental.Accessors;
import solutions.alterego.stackoverflow.test.R;

@Accessors(prefix = "m")
public class MainApplication extends Application {

    @Getter
    private static MainApplication mMainApplication;

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        mMainApplication = this;
        component = DaggerAppComponent.builder().androidModule(new AndroidModule(this)).build();

        setupUil();

        System.setProperty("org.joda.time.DateTimeZone.Provider", "com.alterego.stackoverflow.test.helpers.FastDateTimeZoneProvider");
    }

    private void setupUil() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.ic_action_photo)
            .showImageOnLoading(R.drawable.ic_action_photo)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
            .defaultDisplayImageOptions(defaultOptions)
            .build();

        ImageLoader.getInstance().init(config);
    }

    public static AppComponent component() {
        return component;
    }
}
