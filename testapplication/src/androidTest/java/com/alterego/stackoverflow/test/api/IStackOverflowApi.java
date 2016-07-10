package com.alterego.stackoverflow.test.api;

import com.alterego.stackoverflow.test.data.SearchResponse;

import retrofit2.Call;
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
     * @return {@link SearchResponse} result as an {@link retrofit2.Call}
     */
    @GET("search?order=desc&sort=activity&site=stackoverflow")
    Call<SearchResponse> getSearchResultsNormal(@Query("intitle") String titleSearchTerms, @Query("tagged") String stringDelimitedTags);

    @GET("search?order=desc&sort=activity&site=stackoverflow")
    Observable<SearchResponse> getSearchResultsReactive(@Query("intitle") String titleSearchTerms, @Query("tagged") String stringDelimitedTags);
}
