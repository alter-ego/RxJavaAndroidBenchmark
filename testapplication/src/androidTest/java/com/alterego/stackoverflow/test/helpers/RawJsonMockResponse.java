package com.alterego.stackoverflow.test.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import okhttp3.mockwebserver.MockResponse;

public class RawJsonMockResponse {
    public static MockResponse fromRawResource(InputStream inputStream) throws IOException, JSONException {
        String theString = Utils.toString(inputStream);
        return fromString(theString);
    }

    public static MockResponse fromString(String inputString) throws JSONException {

        JSONObject json = new JSONObject(inputString);
        MockResponse value = new MockResponse();

        value.setBody(json.getJSONObject("body").toString());
        value.setResponseCode(json.optInt("statusCode", 200));

        JSONObject headers = json.optJSONObject("headers");
        if (headers != null) {
            Iterator<String> keys = headers.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                value.addHeader(key, headers.get(key));
            }
        }

        return value;
    }
}
