package com.alterego.stackoverflow.test.api;

import com.alterego.flickr.app.test.R;
import com.alterego.stackoverflow.test.SettingsManager;
import com.alterego.stackoverflow.test.data.SearchResponse;

import java.util.HashMap;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;


public class StackOverflowApiManager {

    private final IStackOverflowApi mStackOverflowService;
    private final SettingsManager mSettingsManager;

    public StackOverflowApiManager(SettingsManager mgr) {

        mSettingsManager = mgr;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(mSettingsManager.getGson()))
                .setEndpoint(mSettingsManager.getParentApplication().getResources().getString(R.string.server))
                .setErrorHandler(new StackOverflowApiErrorHandler(mSettingsManager.getLogger()))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("StackOverflowApiManager"))
                .build();

        mStackOverflowService = restAdapter.create(IStackOverflowApi.class);

    }

    public Observable<SearchResponse> doSearchForTitle(String title) {
        mSettingsManager.getLogger().debug("StackOverflowApiManager doSearchForTitle looking for title = " + title);
        return mStackOverflowService.getSearchResults(title);

    }

}
