package com.alterego.stackoverflow.test;

import android.app.Activity;
import android.app.Application;

import com.alterego.advancedandroidlogger.implementations.NullAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.stackoverflow.test.api.StackOverflowApiManager;
import com.alterego.stackoverflow.test.helpers.DateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class SettingsManager {

    private final ImageLoaderConfiguration mImageLoaderConfiguration;
    private static DateTimeSerializer dateSerializer = new DateTimeSerializer(ISODateTimeFormat.dateTimeParser().withZoneUTC());
    @Getter
    private final StackOverflowApiManager mStackOverflowApiManager;

    @Getter
    Activity mParentActivity;
    @Getter
    @Setter
    Application mParentApplication;
    @Getter
    @Setter
    IAndroidLogger mLogger = NullAndroidLogger.instance;
    @Getter
    private Gson mGson = new GsonBuilder().registerTypeAdapter(DateTime.class, dateSerializer).create();

    public SettingsManager(Application app, IAndroidLogger logger, ImageLoaderConfiguration imageLoaderConfig) {
        setLogger(logger);
        setParentApplication(app);
        mImageLoaderConfiguration = imageLoaderConfig;
        mStackOverflowApiManager = new StackOverflowApiManager(this);
        ImageLoader.getInstance().init(mImageLoaderConfiguration);
    }

    public void setParentActivity(Activity act) {
        mParentActivity = act;
    }
}
