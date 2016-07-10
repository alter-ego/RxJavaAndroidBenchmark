package com.alterego.stackoverflow.test.question;

import com.alterego.stackoverflow.test.data.Answer;
import com.alterego.stackoverflow.test.data.Question;
import com.alterego.stackoverflow.test.helpers.DateHelper;

import org.joda.time.DateTimeZone;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import solutions.alterego.stackoverflow.test.R;

public class AnswersListAdapter extends ArrayAdapter<Question> {

    private List<Answer> mAnswers;
    private final Activity mContext;

    public AnswersListAdapter(Activity context, int resource, List<Answer> items) {
        super(context, resource);
        mContext = context;
        mAnswers = items;
    }

    static class ViewHolder {
        @BindView(R.id.AnswerBody)
        TextView answerBody;
        @BindView(R.id.AuthorName)
        TextView authorName;
        @BindView(R.id.CreationDate)
        TextView creationDate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return mAnswers != null ? mAnswers.size() : 0;
    }

    @Override
    public boolean isEmpty() {
        return (mAnswers == null || mAnswers.size() == 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.fragment_comment_listitem, null);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.authorName.setText(mAnswers.get(position).getOwner().getDisplayName());
        String result = DateHelper.convertDateToString(mContext, mAnswers.get(position).getCreationDate(), Locale.getDefault(), DateTimeZone.getDefault(), "timeDate");
        holder.creationDate.setText(result);
        holder.answerBody.setText(Html.fromHtml(mAnswers.get(position).getBody()));

        return view;
    }
}
