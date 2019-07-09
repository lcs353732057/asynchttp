package com.lcs.asynchttp.library;

import java.io.InputStream;

public interface CallbackListener {
    void onSuccess(InputStream inputStream);

    void onFailure();
}
