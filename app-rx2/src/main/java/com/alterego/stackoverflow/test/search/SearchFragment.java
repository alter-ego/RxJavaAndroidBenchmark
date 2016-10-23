package com.alterego.stackoverflow.test.search;

import com.google.gson.Gson;

import com.alterego.stackoverflow.test.Logger;
import com.alterego.stackoverflow.test.MainApplication;
import com.alterego.stackoverflow.test.OnFragmentInteractionListener;
import com.alterego.stackoverflow.test.api.StackOverflowApiManager;
import com.alterego.stackoverflow.test.data.Question;
import com.alterego.stackoverflow.test.data.SearchResponse;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import solutions.alterego.stackoverflow.test.R;


public class SearchFragment extends Fragment {

    private static final List<String> SEARCH_ARGUMENTS = Arrays.asList("android", "rxjava", "countdownlatch", "multithreading");

    private static final String FRAGMENT_TITLE = "Search";

    private OnFragmentInteractionListener mListener;

    private Disposable searchSubscription;

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
                performSearch();
            }
        });

        return view;
    }

    private void performSearch() {
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchButton.setEnabled(false);
        mNoResultsText.setVisibility(View.INVISIBLE);

        if (searchSubscription != null && !searchSubscription.isDisposed()) {
            searchSubscription.dispose();
        }

        final long startMillis = System.currentTimeMillis();

        Observable.zip(
                stackOverflowApiManager.doSearchForTitleAndTags(SEARCH_ARGUMENTS.get(0), ""),
                stackOverflowApiManager.doSearchForTitleAndTags(SEARCH_ARGUMENTS.get(1), ""),
                stackOverflowApiManager.doSearchForTitleAndTags(SEARCH_ARGUMENTS.get(2), ""),
                stackOverflowApiManager.doSearchForTitleAndTags(SEARCH_ARGUMENTS.get(3), ""),
                new Function4<SearchResponse, SearchResponse, SearchResponse, SearchResponse, List<Question>>() {
                    @Override
                    public List<Question> apply(SearchResponse searchResponse,
                            SearchResponse searchResponse2,
                            SearchResponse searchResponse3,
                            SearchResponse searchResponse4) {

                        logger.getInstance().info("time to receive all responses (ms) = " + (System.currentTimeMillis() - startMillis));

                        List<Question> result = new ArrayList<Question>();
                        result.addAll(searchResponse.getQuestions());
                        result.addAll(searchResponse2.getQuestions());
                        result.addAll(searchResponse3.getQuestions());
                        result.addAll(searchResponse4.getQuestions());

                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(questionSearchObserver);
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
    public void onStop() {
        super.onStop();
        searchSubscription.dispose();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Observer<List<Question>> questionSearchObserver = new Observer<List<Question>>() {
        @Override
        public void onComplete() {
            logger.getInstance().info("SearchFragment questionSearchObserver finished with search");
        }

        @Override
        public void onError(java.lang.Throwable throwable) {
            logger.getInstance().error("SearchFragment questionSearchObserver error receiving search results = " + throwable.toString());
            mProgressBar.setVisibility(View.GONE);
            mNoResultsText.setVisibility(View.VISIBLE);
            mNoResultsText.setText(getString(R.string.search_error));
            mSearchButton.setEnabled(true);
        }

        @Override
        public void onSubscribe(Disposable d) {
            searchSubscription = d;
        }

        @Override
        public void onNext(List<Question> searchResponse) {
            logger.getInstance().info("SearchFragment questionSearchObserver search results size = " + searchResponse.size());
            mProgressBar.setVisibility(View.GONE);
            mSearchButton.setEnabled(true);

            String json_string = gson.toJson(searchResponse);

            if (searchResponse.size() > 0) {
                if (mListener != null) {
                    Fragment fragment_to_open = QuestionsFragment.newInstance(json_string);
                    mListener.onRequestOpenFragment(fragment_to_open, "opening results");
                }
            } else {
                mNoResultsText.setVisibility(View.VISIBLE);
            }
        }
    };
}
