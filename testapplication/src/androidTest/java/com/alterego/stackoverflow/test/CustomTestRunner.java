package com.alterego.stackoverflow.test;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class CustomTestRunner extends android.support.test.runner.AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        return super.newApplication(cl, TestMainApplication.class.getName(), context);
    }

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
    }
}