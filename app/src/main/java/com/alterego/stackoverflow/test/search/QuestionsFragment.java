package com.alterego.stackoverflow.test.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.alterego.stackoverflow.test.MainApplication;
import com.alterego.stackoverflow.test.OnFragmentInteractionListener;
import com.alterego.flickr.app.test.R;
import com.alterego.stackoverflow.test.SettingsManager;
import com.alterego.stackoverflow.test.data.Question;
import com.alterego.stackoverflow.test.data.SearchResponse;
import com.alterego.stackoverflow.test.question.QuestionFragment;

import java.util.ArrayList;
import java.util.List;

public class QuestionsFragment extends Fragment {

    private static final String SEARCH_RESULT = "search_result";

    private SettingsManager mSettingsManager;
    private OnFragmentInteractionListener mListener;
    private List<Question> mQuestions = new ArrayList<>();

    private AbsListView mListView;
    private QuestionsListAdapter mAdapter;
    private android.view.ActionMode mActionMode;


    public static QuestionsFragment newInstance(String search_result) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_RESULT, search_result);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsManager = MainApplication.getMainApplication().getSettingsManager();
        mSettingsManager.getLogger().info("SearchResultFragment onCreate");

        if (getArguments() != null) {
            String searchResultJSON = getArguments().getString(SEARCH_RESULT);
            SearchResponse searchResultObject = mSettingsManager.getGson().fromJson(searchResultJSON, SearchResponse.class);
            mQuestions = searchResultObject.getQuestions();
        }

        mAdapter = new QuestionsListAdapter(mSettingsManager.getParentActivity(), R.layout.fragment_searchresult_listitem, mQuestions);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(selectQuestionListener);

        return view;
    }

    AdapterView.OnItemClickListener selectQuestionListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (mListener != null) {
                Fragment fragment_to_open = QuestionFragment.newInstance(mQuestions.get(position));
                mListener.onRequestOpenFragment(fragment_to_open, "Question: " + mQuestions.get(position).getTitle());
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
