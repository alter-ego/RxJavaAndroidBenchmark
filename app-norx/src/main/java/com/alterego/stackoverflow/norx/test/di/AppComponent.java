package com.alterego.stackoverflow.norx.test.di;


import com.alterego.stackoverflow.norx.test.MainActivity;
import com.alterego.stackoverflow.norx.test.question.AnswersFragment;
import com.alterego.stackoverflow.norx.test.question.CommentsFragment;
import com.alterego.stackoverflow.norx.test.question.QuestionFragment;
import com.alterego.stackoverflow.norx.test.search.SearchFragment;
import com.alterego.stackoverflow.norx.test.search.QuestionsFragment;

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
