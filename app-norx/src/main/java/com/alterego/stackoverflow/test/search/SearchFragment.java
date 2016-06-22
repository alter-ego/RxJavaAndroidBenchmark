package com.alterego.stackoverflow.test.search;

import android.app.Activity;
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

import com.alterego.stackoverflow.test.MainApplication;
import com.alterego.stackoverflow.test.OnFragmentInteractionListener;
import com.alterego.flickr.app.test.R;
import com.alterego.stackoverflow.test.SettingsManager;
import com.alterego.stackoverflow.test.data.SearchResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SearchFragment extends Fragment {

    private static final String FRAGMENT_TITLE = "Search";
    private static final String LAST_SEARCH = "last_search";

    private String mLastSearch;
    private OnFragmentInteractionListener mListener;
    private Subscription mSearchSubscription;
    private SettingsManager mSettingsManager;

    @BindView(R.id.search_edit_text)
    EditText mEditText;
    @BindView(R.id.search_button)
    Button mSearchButton;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.search_text_noresults)
    TextView mNoResultsText;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastSearch = savedInstanceState.getString(LAST_SEARCH);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSettingsManager = MainApplication.getMainApplication().getSettingsManager();
        mSettingsManager.getLogger().info("SearchFragment onCreateView");

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        if (mLastSearch != null)
            mEditText.setText(mLastSearch);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch(result) {
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
        if (mSearchSubscription != null && !mSearchSubscription.isUnsubscribed())
            mSearchSubscription.unsubscribe();

        String searchtext = mEditText.getText().toString();
        mSettingsManager.getLogger().info("SearchFragment searching for = " + searchtext);
        mSearchSubscription = mSettingsManager.getStackOverflowApiManager()
                .doSearchForTitle(searchtext)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(questionSearchObserver);
    }

    @Override
    public void onAttach(Activity activity) {
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
        if (mEditText!=null)
            searchtext = mEditText.getText().toString();

        if (searchtext != null && !searchtext.equals(""))
            outState.putString(LAST_SEARCH, searchtext);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSearchSubscription != null) {
            mSearchSubscription.unsubscribe();
        }
    }

    private Observer<SearchResponse> questionSearchObserver = new Observer<SearchResponse>() {
        @Override
        public void onCompleted() {
            mSettingsManager.getLogger().info("SearchFragment questionSearchObserver finished with search");
        }

        @Override
        public void onError(Throwable throwable) {
            mSettingsManager.getLogger().error("SearchFragment questionSearchObserver error receiving search results = " + throwable.toString());
            mProgressBar.setVisibility(View.GONE);
            mNoResultsText.setVisibility(View.VISIBLE);
            mNoResultsText.setText(getString(R.string.search_error));
            mSearchButton.setEnabled(true);
        }

        @Override
        public void onNext(SearchResponse searchResponse) {
            mSettingsManager.getLogger().info("SearchFragment questionSearchObserver search results = " + searchResponse.toString());
            mProgressBar.setVisibility(View.GONE);
            mSearchButton.setEnabled(true);

            String json_string = mSettingsManager.getGson().toJson(searchResponse);
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
    };


}
