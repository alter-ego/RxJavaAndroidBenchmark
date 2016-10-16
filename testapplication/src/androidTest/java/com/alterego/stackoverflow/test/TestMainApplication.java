package com.alterego.stackoverflow.test;

import com.alterego.stackoverflow.test.di.AndroidTestComponent;

import android.app.Application;

import lombok.Getter;
import lombok.Setter;

public class TestMainApplication extends Application {

    @Getter
    @Setter
    private static AndroidTestComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setComponent(buildComponentAndInject());
        System.setProperty("org.joda.time.DateTimeZone.Provider", "com.alterego.stackoverflow.test.helpers.FastDateTimeZoneProvider");
    }

    public AndroidTestComponent buildComponentAndInject() {
        return AndroidTestComponent.Initializer.init(this);
    }

}
