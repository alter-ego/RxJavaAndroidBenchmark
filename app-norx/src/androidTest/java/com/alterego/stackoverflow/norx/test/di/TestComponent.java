package com.alterego.stackoverflow.norx.test.di;

import com.alterego.stackoverflow.norx.test.api.StackOverflowApiManagerTest;

import android.app.Application;

import javax.inject.Singleton;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Singleton
@dagger.Component(modules = {TestProvidersModule.class})
public interface TestComponent extends AppComponent {

    void inject(StackOverflowApiManagerTest stackOverflowApiManagerTest);

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    final class Initializer {

        public static TestComponent init(Application app) {
            return DaggerTestComponent.builder()
                    .testProvidersModule(new TestProvidersModule(app))
                    .build();
        }
    }
}