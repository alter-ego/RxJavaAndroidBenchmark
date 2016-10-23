package com.alterego.stackoverflow.test.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="m")
public class SearchResponse {

    @Expose
    @Getter
    @SerializedName("items")
    private List<Question> mQuestions;

}
