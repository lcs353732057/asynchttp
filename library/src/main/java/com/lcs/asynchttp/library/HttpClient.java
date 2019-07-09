package com.lcs.asynchttp.library;

public class HttpClient {
    public static <T, M> void sendJsonRequest(
            T requestData, String url, Class<M> response, IJsonDataTransforListener listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbackListener<>(response, listener);
        HttpTask ht = new HttpTask(url, requestData, httpRequest, callbackListener);
        ThreadPoolManager.getInstance().addTask(ht);
    }
}
