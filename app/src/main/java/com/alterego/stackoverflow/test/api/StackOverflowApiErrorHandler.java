package com.alterego.stackoverflow.test.api;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

class StackOverflowApiErrorHandler implements ErrorHandler {

    private final IAndroidLogger mLogger;

    StackOverflowApiErrorHandler(IAndroidLogger logger) {
        mLogger = logger;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        try {
            return new StackOverflowApiException(cause);
        } catch (Exception e) {
            mLogger.error("StackOverflowApiErrorHandler cannot read body, error = " + cause);
        }
        return cause;
    }



    private class StackOverflowApiException extends Throwable {

        public StackOverflowApiException(RetrofitError cause) {
            try {
                mLogger.error("StackOverflowApiException error = " + cause.getMessage() + ", response = " + cause.getResponse().getBody().in().toString());
            } catch (Exception e) {
                mLogger.error("StackOverflowApiException error = " + cause.getMessage() + ", can't read the response!");
            }
        }
    }
}