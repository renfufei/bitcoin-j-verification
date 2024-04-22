package com.cncounter.bitcoinjverification.tools;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;

// OKHttp工具类
// 参考: @link{ https://square.github.io/okhttp/ }
public class HttpUtilsOK {
    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            String respText = responseBody.string();
            //
            return respText;
        } catch (IOException e) {
            throw new RuntimeException("请求URL失败; url:" + url, e);
        }
    }

    public static final MediaType JSON = MediaType.get("application/json");

    public static String post(String url, JSONObject data) {
        RequestBody body = RequestBody.create(data.toJSONString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            String respText = responseBody.string();
            //
            return respText;
        } catch (IOException e) {
            throw new RuntimeException("请求URL失败; url:" + url, e);
        }
    }
}
