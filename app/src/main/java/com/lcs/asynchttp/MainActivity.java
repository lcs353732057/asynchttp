package com.lcs.asynchttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lcs.asynchttp.bean.JsonRootBean;
import com.lcs.asynchttp.library.HttpClient;
import com.lcs.asynchttp.library.IJsonDataTransforListener;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    //    String url =
    //
    // "https://api.uyizhan.com/station/banners/by-type/APP_INDEX?size=5&sort=publishTime,desc";

    //    String url =
    // "http://v.juhe.cn/toutiao/index?type=top&key=4b7c9474c567d7c7f7ae1975213d15ba";//正确地址

    String url = "http://vdsfsdfsd"; // 错误地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpClient.sendJsonRequest(
                null,
                url,
                JsonRootBean.class,
                new IJsonDataTransforListener<JsonRootBean>() {

                    @Override
                    public void onSuccess(JsonRootBean bannerBean) {
                        Log.e(TAG, bannerBean.toString() + "");
                    }

                    @Override
                    public void onFailure() {}
                });
    }
}
