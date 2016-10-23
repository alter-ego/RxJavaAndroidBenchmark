package com.alterego.stackoverflow.test.api;

import com.alterego.stackoverflow.test.data.SearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This is the API for StackOverflow.
 */
public interface IStackOverflowApi {

    /**
     * This call executes the search and retrieves the found questions
     *
     * @param titleSearchTerms Search terms
     * @return {@link SearchResponse} result as an {@link io.reactivex.Observable}
     */
    @GET("search?order=desc&sort=activity&site=stackoverflow&filter=!3yXvh9)gd0IKKXn31")
    Observable<SearchResponse> getSearchResults(@Query("intitle") String titleSearchTerms, @Query("tagged") String stringDelimitedTags);
}
