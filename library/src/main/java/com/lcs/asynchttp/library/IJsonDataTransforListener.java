package com.lcs.asynchttp.library;

public interface IJsonDataTransforListener<T> {

    void onSuccess(T t);

    void onFailure();
}
