package com.alterego.stackoverflow.norx.test.question;

import com.alterego.stackoverflow.norx.test.Logger;
import com.alterego.stackoverflow.norx.test.MainApplication;
import com.alterego.stackoverflow.norx.test.data.Comment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import solutions.alterego.stackoverflow.norx.test.R;

public class CommentsFragment extends Fragment {

    private static final String COMMENTS = "comments";

    @Inject
    Logger logger;

    private List<Comment> mComments = new ArrayList<>();

    private AbsListView mListView;

    private CommentsListAdapter mAdapter;

    public static CommentsFragment newInstance(ArrayList<Comment> comments) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(COMMENTS, comments);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.component().inject(this);

        logger.getInstance().info("SearchResultFragment onCreate");

        if (getArguments() != null) {
            mComments = getArguments().getParcelableArrayList(COMMENTS);
        }

        mAdapter = new CommentsListAdapter(getActivity(), R.layout.fragment_searchresult_listitem, mComments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        return view;
    }

}
