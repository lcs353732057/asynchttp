package com.lcs.asynchttp.library;

public interface IHttpRequest {
    void setUrl(String url);

    void setData(byte[] data);

    void setListener(CallbackListener listener);

    void execute();
}
