package com.alterego.stackoverflow.norx.test.di;

import android.app.Application;

import dagger.Module;

@Module
public class AndroidTestProvidersModule extends AndroidModule {

    public AndroidTestProvidersModule(Application app) {
        super(app);
    }

}
