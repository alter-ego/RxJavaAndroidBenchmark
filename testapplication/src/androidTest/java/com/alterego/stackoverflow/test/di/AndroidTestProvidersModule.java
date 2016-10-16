package com.alterego.stackoverflow.test.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.alterego.advancedandroidlogger.implementations.DetailedAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.stackoverflow.test.Logger;
import com.alterego.stackoverflow.test.helpers.DateTimeSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import android.app.Application;
import android.content.Context;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidTestProvidersModule {

    private final Application application;

    private static final String LOGGING_TAG = "StackOverflowTest";

    public AndroidTestProvidersModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Logger providesLogger() {
        return new Logger(new DetailedAndroidLogger(LOGGING_TAG, IAndroidLogger.LoggingLevel.VERBOSE));
    }

    @Provides
    @Singleton
    @Named("cacheDir")
    File provideCacheDir(Context context) {
        return context.getCacheDir();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        DateTimeSerializer dateSerializer = new DateTimeSerializer(ISODateTimeFormat.dateTimeParser().withZoneUTC());
        return new GsonBuilder().registerTypeAdapter(DateTime.class, dateSerializer).create();
    }

}
