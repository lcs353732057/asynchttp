package com.lcs.asynchttp.library;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable, Delayed {
    private IHttpRequest httpRequest;

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    private long delayTime;

    private int retryTime;

    public HttpTask(
            String url, T requestData, IHttpRequest httpRequest, CallbackListener listener) {
        httpRequest.setUrl(url);
        httpRequest.setListener(listener);
        try {
            httpRequest.setData(new Gson().toJson(requestData).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.httpRequest = httpRequest;
    }

    @Override
    public void run() {
        try {
            this.httpRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }

    @Override
    public long getDelay(@NonNull TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@NonNull Delayed o) {
        return 0;
    }
}
