package com.alterego.stackoverflow.test.question;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.alterego.flickr.app.test.R;
import com.alterego.stackoverflow.test.MainApplication;
import com.alterego.stackoverflow.test.OnFragmentInteractionListener;
import com.alterego.stackoverflow.test.SettingsManager;
import com.alterego.stackoverflow.test.data.Question;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionFragment extends Fragment {

    private static final String QUESTION = "question";

    private SettingsManager mSettingsManager;
    private OnFragmentInteractionListener mListener;
    private Question mQuestion;

    @BindView(R.id.AnswerBody)
    TextView mQuestionTitle;
    @BindView(R.id.QuestionContent)
    WebView mQuestionContent;
    @BindView(R.id.ShowComments)
    Button mShowComments;
    @BindView(R.id.ShowAnswers)
    Button mShowAnswers;


    public static QuestionFragment newInstance(Question question) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsManager = MainApplication.getMainApplication().getSettingsManager();
        mSettingsManager.getLogger().info("QuestionFragment onCreate");

        if (getArguments() != null) {
            mQuestion = getArguments().getParcelable(QUESTION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);
        mQuestionTitle.setText(Html.fromHtml(mQuestion.getTitle()));
        mQuestionContent.loadData(mQuestion.getBody(), "text/html", "UTF-8");
        if (mQuestion.isAnswered()) {
            mShowAnswers.setEnabled(true);
            mShowAnswers.setOnClickListener(showAnswersListener);
        }

        if (mQuestion.getComments() != null && mQuestion.getComments().size() > 0) {
            mShowComments.setEnabled(true);
            mShowComments.setOnClickListener(showCommentsListener);
        }
        return view;
    }

    View.OnClickListener showAnswersListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                Fragment fragment_to_open = AnswersFragment.newInstance(new ArrayList<>(mQuestion.getAnswers()));
                mListener.onRequestOpenFragment(fragment_to_open, "Answers: " + mQuestion.getTitle());
            }
        }
    };

    View.OnClickListener showCommentsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                Fragment fragment_to_open = CommentsFragment.newInstance(new ArrayList<>(mQuestion.getComments()));
                mListener.onRequestOpenFragment(fragment_to_open, "Comments: " + mQuestion.getTitle());
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
