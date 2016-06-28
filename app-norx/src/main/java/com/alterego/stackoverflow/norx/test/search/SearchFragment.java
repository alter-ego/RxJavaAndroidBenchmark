package com.alterego.stackoverflow.norx.test.search;

import com.alterego.stackoverflow.norx.test.data.Question;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import solutions.alterego.stackoverflow.norx.test.R;

public class SearchFragment extends Fragment {

    private static final String FRAGMENT_TITLE = "Search";

    private static final String LAST_SEARCH = "last_search";

    private String mLastSearch;

    private OnFragmentInteractionListener mListener;

    //private Subscription searchSubscription;

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
        //if (searchSubscription != null && !searchSubscription.isUnsubscribed()) {
        //    searchSubscription.unsubscribe();
        //}

        String searchtext = mEditText.getText().toString();

        //TODO Fix with no rx
        //searchSubscription = stackOverflowApiManager
        //.doSearchForTitle(searchtext)
        //.observeOn(AndroidSchedulers.mainThread())
        //.subscribeOn(Schedulers.io())
        //.subscribe(questionSearchObserver);
        questionSearch(stackOverflowApiManager.doSearchForTitle(searchtext));
        //ArrayList<SearchResponse> searchSubscription = stackOverflowApiManager.doSearchForTitle(searchtext);
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
        //if (searchSubscription != null) {
        //    searchSubscription.unsubscribe();
        //}
    }

    private void questionSearch(SearchResponse searchResponse) {

        mProgressBar.setVisibility(View.GONE);
        mSearchButton.setEnabled(true);
        logger.getInstance().info("SearchFragment questionSearchObserver search results = " + searchResponse.toString());


        String json_string = gson.toJson(searchResponse);
        String searchtext = mEditText.getText().toString();

        if (searchResponse.getQuestions() != null && searchResponse.getQuestions().size() > 0) {
            if (mListener != null) {
                Fragment fragment_to_open = QuestionsFragment.newInstance(json_string);
                mListener.onRequestOpenFragment(fragment_to_open, "Results: " + searchtext);
            }
        } else {
            mNoResultsText.setVisibility(View.VISIBLE);
        }
    }

}

    //TODO Replace with no rx version
    /*private Observer<SearchResponse> questionSearchObserver = new Observer<SearchResponse>() {
        @Override
        public void onCompleted() {
            logger.getInstance().info("SearchFragment questionSearchObserver finished with search");
        }

        @Override
        public void onError(Throwable throwable) {
            logger.getInstance().error("SearchFragment questionSearchObserver error receiving search results = " + throwable.toString());
            mProgressBar.setVisibility(View.GONE);
            mNoResultsText.setVisibility(View.VISIBLE);
            mNoResultsText.setText(getString(R.string.search_error));
            mSearchButton.setEnabled(true);
        }

        @Override
        public void onNext(SearchResponse searchResponse) {
            logger.getInstance().info("SearchFragment questionSearchObserver search results = " + searchResponse.toString());
            mProgressBar.setVisibility(View.GONE);
            mSearchButton.setEnabled(true);

            String json_string = gson.toJson(searchResponse);
            String searchtext = mEditText.getText().toString();

            if (searchResponse.getQuestions() != null && searchResponse.getQuestions().size() > 0) {
                if (mListener != null) {
                    Fragment fragment_to_open = QuestionsFragment.newInstance(json_string);
                    mListener.onRequestOpenFragment(fragment_to_open, "Results: " + searchtext);
                }
            } else {
                mNoResultsText.setVisibility(View.VISIBLE);
            }
        }
    };*/
