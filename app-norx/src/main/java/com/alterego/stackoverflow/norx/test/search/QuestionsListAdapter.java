package com.alterego.stackoverflow.norx.test.search;

import com.alterego.stackoverflow.norx.test.data.Question;
import com.alterego.stackoverflow.norx.test.helpers.DateHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTimeZone;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import solutions.alterego.stackoverflow.norx.test.R;

public class QuestionsListAdapter extends ArrayAdapter<Question> {

    private final List<Question> mQuestions;
    private final Activity mContext;

    public QuestionsListAdapter(Activity context, int resource, List<Question> items) {
        super(context, resource);
        mContext = context;
        mQuestions = items;
    }

    static class ViewHolder {
        @BindView(R.id.AnswerBody)
        TextView questionTitle;
        @BindView(R.id.AuthorName)
        TextView authorName;
        @BindView(R.id.CreationDate)
        TextView questionDate;
        @BindView(R.id.AuthorPhoto)
        ImageView authorPhoto;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return mQuestions != null ? mQuestions.size() : 0;
    }

    @Override
    public boolean isEmpty() {
        return (mQuestions == null || mQuestions.size() == 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.fragment_searchresult_listitem, null);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.questionTitle.setText(Html.fromHtml(mQuestions.get(position).getTitle()));
        holder.authorName.setText(mQuestions.get(position).getOwner().getDisplayName());
        String result = DateHelper
                .convertDateToString(mContext, mQuestions.get(position).getCreationDate(), Locale.getDefault(), DateTimeZone.getDefault(), "timeDate");
        holder.questionDate.setText(result);
        ImageLoader.getInstance().displayImage(mQuestions.get(position).getOwner().getProfileImage(), holder.authorPhoto);

        return view;
    }

    @Override
    public Question getItem(int position) {
        return mQuestions.get(position);
    }
}
