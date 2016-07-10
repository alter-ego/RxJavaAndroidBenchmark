package com.alterego.stackoverflow.norx.test.api;

import com.google.gson.Gson;

import com.alterego.stackoverflow.norx.test.TestMainApplication;
import com.alterego.stackoverflow.norx.test.data.SearchResponse;
import com.alterego.stackoverflow.norx.test.di.AndroidTestComponent;
import com.alterego.stackoverflow.norx.test.helpers.RawJsonMockResponse;

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

@RunWith(AndroidJUnit4.class)
public class StackOverflowApiManagerAndroidTest {

    @Inject
    Gson gson;

    @Inject
    Context mContext;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private StackOverflowApiManager mStackOverflowApiManager;

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        ((AndroidTestComponent) TestMainApplication.getComponent()).inject(this);
        server = new MockWebServer();
        server.start();
        String serverBaseUrl = server.url("/").toString();
        mStackOverflowApiManager = new StackOverflowApiManager(gson, null, serverBaseUrl);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void apiservice_successfully_gets_mocked_search_result() throws Exception {
        MockResponse successfulCachedSettingsMockResponse = RawJsonMockResponse.fromString(StackOverflowApiResponses.SEARCH_RAW_RESPONSE);
        server.enqueue(successfulCachedSettingsMockResponse);

        Call<SearchResponse> call = mStackOverflowApiManager.doSearchForTitleAndTags("", "");
        Response<SearchResponse> response = call.execute();

        Assertions.assertThat(server.getRequestCount()).isEqualTo(1);
        Assertions.assertThat(response.raw().request().headers()).isNotNull();
        Assertions.assertThat(response.body()).isNotNull();
    }

}
