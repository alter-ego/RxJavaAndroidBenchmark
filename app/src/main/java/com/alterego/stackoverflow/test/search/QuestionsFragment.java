package com.alterego.stackoverflow.test.search;

import com.google.gson.Gson;

import com.alterego.stackoverflow.test.Logger;
import com.alterego.stackoverflow.test.MainApplication;
import com.alterego.stackoverflow.test.OnFragmentInteractionListener;
import com.alterego.stackoverflow.test.data.Question;
import com.alterego.stackoverflow.test.data.SearchResponse;
import com.alterego.stackoverflow.test.question.QuestionFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import solutions.alterego.stackoverflow.test.R;

public class QuestionsFragment extends Fragment {

    private static final String SEARCH_RESULT = "search_result";

    @Inject
    Logger logger;

    private OnFragmentInteractionListener mListener;

    private List<Question> mQuestions = new ArrayList<>();

    private AbsListView mListView;

    private QuestionsListAdapter mAdapter;

    private android.view.ActionMode mActionMode;

    @Inject
    Gson gson;

    public static QuestionsFragment newInstance(String search_result) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_RESULT, search_result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.component().inject(this);

        logger.getInstance().info("SearchResultFragment onCreate");

        if (getArguments() != null) {
            String searchResultJSON = getArguments().getString(SEARCH_RESULT);
            SearchResponse searchResultObject = gson.fromJson(searchResultJSON, SearchResponse.class);
            mQuestions = searchResultObject.getQuestions();
        }

        mAdapter = new QuestionsListAdapter(getActivity(), R.layout.fragment_searchresult_listitem, mQuestions);
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
