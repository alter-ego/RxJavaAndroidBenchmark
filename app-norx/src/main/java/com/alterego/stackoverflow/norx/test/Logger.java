package com.alterego.stackoverflow.norx.test;

import com.alterego.advancedandroidlogger.implementations.NullAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

import lombok.Getter;

public class Logger {

    @Getter
    IAndroidLogger instance = NullAndroidLogger.instance;

    public Logger(IAndroidLogger logger) {
        instance = logger;
    }
}
