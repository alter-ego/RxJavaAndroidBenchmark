package com.alterego.stackoverflow.test.di;

import com.alterego.stackoverflow.test.MainActivity;
import com.alterego.stackoverflow.test.question.AnswersFragment;
import com.alterego.stackoverflow.test.question.CommentsFragment;
import com.alterego.stackoverflow.test.question.QuestionFragment;
import com.alterego.stackoverflow.test.search.QuestionsFragment;
import com.alterego.stackoverflow.test.search.SearchFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AndroidModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(AnswersFragment answersFragment);

    void inject(SearchFragment searchFragment);

    void inject(QuestionsFragment questionsFragment);

    void inject(QuestionFragment questionFragment);

    void inject(CommentsFragment commentsFragment);
}
