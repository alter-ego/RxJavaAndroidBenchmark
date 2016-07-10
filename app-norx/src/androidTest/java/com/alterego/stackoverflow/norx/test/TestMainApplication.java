package com.alterego.stackoverflow.norx.test;

import com.alterego.stackoverflow.norx.test.di.AndroidTestComponent;
import com.alterego.stackoverflow.norx.test.di.AppComponent;

import lombok.Getter;
import lombok.Setter;

public class TestMainApplication extends MainApplication {

    @Getter
    @Setter
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        setComponent(buildComponentAndInject());
    }

    public AppComponent buildComponentAndInject() {
        return AndroidTestComponent.Initializer.init(this);
    }

}
