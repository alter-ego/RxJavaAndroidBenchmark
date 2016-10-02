package com.alterego.stackoverflow.norx.test.search;

import com.google.gson.Gson;

import com.alterego.stackoverflow.norx.test.Logger;
import com.alterego.stackoverflow.norx.test.MainApplication;
import com.alterego.stackoverflow.norx.test.OnFragmentInteractionListener;
import com.alterego.stackoverflow.norx.test.api.StackOverflowApiManager;
import com.alterego.stackoverflow.norx.test.data.Question;
import com.alterego.stackoverflow.norx.test.data.SearchResponse;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import solutions.alterego.stackoverflow.norx.test.R;

public class SearchFragment extends Fragment {

    private static final List<String> SEARCH_ARGUMENTS = Arrays.asList("android", "rxjava", "countdownlatch", "multithreading");

    private static final String FRAGMENT_TITLE = "Search";

    private OnFragmentInteractionListener mListener;

    double startTime;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.search_button)
    Button mSearchButton;

    @BindView(R.id.search_text_noresults)
    TextView mNoResultsText;

    @Inject
    StackOverflowApiManager stackOverflowApiManager;

    @Inject
    Logger logger;

    @Inject
    Gson gson;

    List<Question> mResults;

    CountDownLatch mLatch;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    performSearch();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "error getting search results", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void performSearch() throws Exception {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoResultsText.setVisibility(View.INVISIBLE);
        mSearchButton.setEnabled(false);
        mResults = new ArrayList<>();

        startTime = System.currentTimeMillis();
        logger.getInstance().error("Start time in SearchFragment onCreate: ", String.valueOf(startTime));

        mLatch = new CountDownLatch(4);

        questionSearch(stackOverflowApiManager.doSearchForTitle(SEARCH_ARGUMENTS.get(0)));
        questionSearch(stackOverflowApiManager.doSearchForTitle(SEARCH_ARGUMENTS.get(1)));
        questionSearch(stackOverflowApiManager.doSearchForTitle(SEARCH_ARGUMENTS.get(2)));
        questionSearch(stackOverflowApiManager.doSearchForTitle(SEARCH_ARGUMENTS.get(3)));
    }

    private void checkResults() {
        if (mResults != null && mResults.size() > 0) {
            mProgressBar.setVisibility(View.GONE);
            mSearchButton.setEnabled(true);

            if (mListener != null) {
                Fragment fragment_to_open = QuestionsFragment.newInstance(gson.toJson(mResults));
                mListener.onRequestOpenFragment(fragment_to_open, "opening results");
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            mNoResultsText.setVisibility(View.VISIBLE);
            mNoResultsText.setText(getString(R.string.search_error));
            mSearchButton.setEnabled(true);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void questionSearch(final Call<SearchResponse> searchResponse) {

        searchResponse.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                logger.getInstance().info("SearchFragment questionSearchObserver search results = " + response.toString());

                mLatch.countDown();
                mResults.addAll(response.body().getQuestions());

                if (mLatch.getCount() == 0) {
                    logger.getInstance().info("time to receive all responses (ms) = " + (System.currentTimeMillis() - startTime));
                    checkResults();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                logger.getInstance().error("SearchFragment error receiving search results = " + t.toString());

                mLatch.countDown();

                if (mLatch.getCount() == 0) {
                    logger.getInstance().info("time to receive all responses (ms) = " + (System.currentTimeMillis() - startTime));
                    checkResults();
                }
            }
        });
    }

}
