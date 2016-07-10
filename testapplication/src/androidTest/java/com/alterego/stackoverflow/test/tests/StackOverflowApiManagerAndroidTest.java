package com.alterego.stackoverflow.test.tests;

import com.google.gson.Gson;

import com.alterego.stackoverflow.test.TestMainApplication;
import com.alterego.stackoverflow.test.api.StackOverflowApiManager;
import com.alterego.stackoverflow.test.api.StackOverflowApiResponses;
import com.alterego.stackoverflow.test.data.SearchResponse;
import com.alterego.stackoverflow.test.helpers.RawJsonMockResponse;
import com.alterego.stackoverflow.test.rules.Repeat;
import com.alterego.stackoverflow.test.rules.RepeatRule;
import com.tspoon.benchit.Benchit;

import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import javax.inject.Inject;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

@RunWith(AndroidJUnit4.class)
public class StackOverflowApiManagerAndroidTest {

    @Inject
    Gson gson;

    @Inject
    Context mContext;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    private StackOverflowApiManager mStackOverflowApiManager;

    private MockWebServer server;

    final TestScheduler testScheduler = new TestScheduler();

    @Before
    public void setUp() throws Exception {
        TestMainApplication.getComponent().inject(this);
        server = new MockWebServer();
        server.start();
        String serverBaseUrl = server.url("/").toString();
        mStackOverflowApiManager = new StackOverflowApiManager(gson, null, serverBaseUrl, testScheduler);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
        try {
            Benchit.analyze("simple-search-result-call-normal").log();
        } catch (Exception e) {

        }
        try {
            Benchit.analyze("simple-search-result-call-reactive").log();
        } catch (Exception e) {

        }
    }

    @Test
    @Repeat(times = 100)
    public void apiservice_gets_mocked_search_result_normal() throws Exception {
        MockResponse successfulCachedSettingsMockResponse = RawJsonMockResponse.fromString(StackOverflowApiResponses.SEARCH_RAW_RESPONSE);
        server.enqueue(successfulCachedSettingsMockResponse);

        Benchit.begin("simple-search-result-call-normal");
        Call<SearchResponse> call = mStackOverflowApiManager.doSearchForTitleAndTagsNormal("", "");
        Response<SearchResponse> response = call.execute();
        Benchit.end("simple-search-result-call-normal");

        Assertions.assertThat(server.getRequestCount()).isEqualTo(1);
        Assertions.assertThat(response.raw().request().headers()).isNotNull();
        Assertions.assertThat(response.body()).isNotNull();
    }

    @Test
    @Repeat(times = 100)
    public void apiservice_gets_mocked_search_result_reactive() throws Exception {
        MockResponse successfulCachedSettingsMockResponse = RawJsonMockResponse.fromString(StackOverflowApiResponses.SEARCH_RAW_RESPONSE);
        server.enqueue(successfulCachedSettingsMockResponse);

        Benchit.begin("simple-search-result-call-reactive");
        TestSubscriber testSubscriber = TestSubscriber.create();
        mStackOverflowApiManager.doSearchForTitleReactive("", "").subscribe(testSubscriber);
        testScheduler.triggerActions();
        Benchit.end("simple-search-result-call-reactive");

        Assertions.assertThat(server.getRequestCount()).isEqualTo(1);
        testSubscriber.assertValueCount(1);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
    }

}
