package com.alterego.stackoverflow.norx.test.api;

import com.google.gson.Gson;

import com.alterego.stackoverflow.norx.test.data.SearchResponse;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class StackOverflowApiManager {

    private static final long CONNECTION_TIMEOUT = 30;

    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 2 * 1000;

    private final IStackOverflowApi service;

    @Inject
    public StackOverflowApiManager(Gson gson, @Named("cacheDir") File cacheDir, @Named("api_baseurl") String baseUrl) {

        Retrofit restAdapter = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(getOkHttpClient(cacheDir))
            .build();

        service = restAdapter.create(IStackOverflowApi.class);
    }

    public Observable<SearchResponse> doSearchForTitle(String title) {
        return service.getSearchResults(title);
    }

    private OkHttpClient getOkHttpClient(File baseDir) {
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okClientBuilder.addInterceptor(httpLoggingInterceptor);

        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            okClientBuilder.cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
        }

        okClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        okClientBuilder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        return okClientBuilder.build();
    }
}
