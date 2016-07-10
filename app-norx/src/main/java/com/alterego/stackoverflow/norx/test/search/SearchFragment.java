package com.alterego.stackoverflow.norx.test.search;

import com.google.gson.Gson;

import com.alterego.stackoverflow.norx.test.Logger;
import com.alterego.stackoverflow.norx.test.MainApplication;
import com.alterego.stackoverflow.norx.test.OnFragmentInteractionListener;
import com.alterego.stackoverflow.norx.test.api.StackOverflowApiManager;
import com.alterego.stackoverflow.norx.test.data.SearchResponse;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import solutions.alterego.stackoverflow.norx.test.R;

public class SearchFragment extends Fragment {

    private static final String FRAGMENT_TITLE = "Search";

    private static final String LAST_SEARCH = "last_search";

    private String mLastSearch;

    private OnFragmentInteractionListener mListener;

    double startTime;

    @BindView(R.id.search_edit_text)
    EditText mEditText;

    @BindView(R.id.search_button)
    Button mSearchButton;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.search_text_noresults)
    TextView mNoResultsText;

    @Inject
    StackOverflowApiManager stackOverflowApiManager;

    @Inject
    Logger logger;

    @Inject
    Gson gson;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.component().inject(this);

        startTime = System.currentTimeMillis();
        logger.getInstance().error("Start time in SearchFragment onCreate: ", String.valueOf(startTime));

        if (savedInstanceState != null) {
            mLastSearch = savedInstanceState.getString(LAST_SEARCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        if (mLastSearch != null) {
            mEditText.setText(mLastSearch);
        }

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        performSearch();
                        break;
                }
                return false;
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        return view;
    }

    private void performSearch() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoResultsText.setVisibility(View.INVISIBLE);
        mSearchButton.setEnabled(false);

        String searchtext = mEditText.getText().toString();

        questionSearch(stackOverflowApiManager.doSearchForTitle(searchtext));
        double performSearchTime = (System.currentTimeMillis() - startTime) / 1000;
        logger.getInstance().error("Timing in performSearch(): ", String.valueOf(performSearchTime));
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mListener.setActionBarTitle(FRAGMENT_TITLE);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.setActionBarTitle(FRAGMENT_TITLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String searchtext = "";
        super.onSaveInstanceState(outState);
        if (mEditText != null) {
            searchtext = mEditText.getText().toString();
        }

        if (searchtext != null && !searchtext.equals("")) {
            outState.putString(LAST_SEARCH, searchtext);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void questionSearch(final Call<SearchResponse> searchResponse) {

        searchResponse.enqueue(new Callback<SearchResponse>(){

            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                logger.getInstance().info("SearchFragment questionSearchObserver search results = " + response.toString());
                mProgressBar.setVisibility(View.GONE);
                mSearchButton.setEnabled(true);

                String json_string = gson.toJson(response.body());
                String searchtext = mEditText.getText().toString();

                if (response.body().getQuestions() != null && response.body().getQuestions().size() > 0) {
                    if (mListener != null) {
                        Fragment fragment_to_open = QuestionsFragment.newInstance(json_string);
                        mListener.onRequestOpenFragment(fragment_to_open, "Results: " + searchtext);
                    }
                } else {
                    mNoResultsText.setVisibility(View.VISIBLE);
                }

                double questionSearchTime = (System.currentTimeMillis() - startTime)/1000;
                logger.getInstance().error("Timing in questionSearch() onResponse: ", String.valueOf(questionSearchTime));
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                logger.getInstance().error("SearchFragment error receiving search results = " + t.toString());
                mProgressBar.setVisibility(View.GONE);
                mNoResultsText.setVisibility(View.VISIBLE);
                mNoResultsText.setText(getString(R.string.search_error));
                mSearchButton.setEnabled(true);
                double questionSearchFailureTime = (System.currentTimeMillis() - startTime)/1000;
                logger.getInstance().error("Timing in questionSearch() onFailure: ", String.valueOf(questionSearchFailureTime));
            }
        });
    }

}
