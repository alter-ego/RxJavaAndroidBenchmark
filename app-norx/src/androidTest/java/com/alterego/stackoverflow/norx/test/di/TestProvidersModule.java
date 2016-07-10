package com.alterego.stackoverflow.norx.test.di;

import android.app.Application;

import dagger.Module;

@Module
public class TestProvidersModule extends AndroidModule {

    public TestProvidersModule(Application app) {
        super(app);
    }

}
