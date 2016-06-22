package com.alterego.stackoverflow.norx.test.api;

import com.alterego.stackoverflow.norx.test.data.SearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * This is the API for StackOverflow.
 */
public interface IStackOverflowApi {

    /**
     * This call executes the search and retrieves the found questions
     *
     * @param titleSearchTerms Search terms
     * @return {@link SearchResponse} result as an {@link rx.Observable}
     */
    //http://api.stackexchange.com/2.2/search?order=desc&sort=activity&intitle=android&site=stackoverflow
    @GET("search?order=desc&sort=activity&site=stackoverflow&filter=!3yXvh9)gd0IKKXn31")
    Observable<SearchResponse> getSearchResults(@Query("intitle") String titleSearchTerms);
}
