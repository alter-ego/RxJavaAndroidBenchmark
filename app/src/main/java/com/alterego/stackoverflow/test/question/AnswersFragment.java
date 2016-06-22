package com.alterego.stackoverflow.test.question;

import com.alterego.flickr.app.test.R;
import com.alterego.stackoverflow.test.MainApplication;
import com.alterego.stackoverflow.test.OnFragmentInteractionListener;
import com.alterego.stackoverflow.test.data.Answer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

public class AnswersFragment extends Fragment {

    private static final String ANSWERS = "answers";

    private OnFragmentInteractionListener mListener;

    private List<Answer> mAnswers = new ArrayList<>();

    private AbsListView mListView;

    private AnswersListAdapter mAdapter;


    public static AnswersFragment newInstance(ArrayList<Answer> answers) {
        AnswersFragment fragment = new AnswersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ANSWERS, answers);
        fragment.setArguments(args);
        return fragment;
    }

    public AnswersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.component().inject(this);
        if (getArguments() != null) {
            mAnswers = getArguments().getParcelableArrayList(ANSWERS);
        }
        mAdapter = new AnswersListAdapter(getActivity(), R.layout.fragment_answer_listitem, mAnswers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(selectCommentListener);

        return view;
    }

//    AdapterView.OnItemClickListener selectCommentListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            if (mListener != null) {
//                Fragment fragment_to_open = QuestionFragment.newInstance(mAnswers.get(position));
//                mListener.onRequestOpenFragment(fragment_to_open, "Question: " + mAnswers.get(position).getTitle());
//            }
//        }
//    };

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
