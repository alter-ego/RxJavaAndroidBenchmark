package com.alterego.stackoverflow.norx.test.di;

import com.alterego.stackoverflow.norx.test.api.StackOverflowApiManagerAndroidTest;

import android.app.Application;

import javax.inject.Singleton;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Singleton
@dagger.Component(modules = {AndroidTestProvidersModule.class})
public interface AndroidTestComponent extends AppComponent {

    void inject(StackOverflowApiManagerAndroidTest stackOverflowApiManagerAndroidTest);

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    final class Initializer {

        public static AndroidTestComponent init(Application app) {
            return DaggerAndroidTestComponent.builder()
                    .androidTestProvidersModule(new AndroidTestProvidersModule(app))
                    .build();
        }
    }
}