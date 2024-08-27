package com.wf.imaotai.util;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HttpUtil {

    public static String test() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"itemInfoList\":[{\"itemId\":\"10941\",\"count\":1}],\r\n    \"actParam\":\"IdiwwdtRdEBhdeHkaJbq1KaW+LwQpVuZhH2uIaRKC41kqQyRplBiH4D9NXHDRD0y1D3syYKc3xmQzsNpLGCJGD4RX6OPmIW4TPXFadeIWIrT/kp1ZV/3UqvTUXIV5gPKm/a7cqI6gLTuYxlGJ8K+IRZUwDwj1wbHxUzGdN+PWks=\",\r\n    \"sessionId\":\"1163\",\r\n    \"shopId\":\"231310108005\",\r\n    \"userId\":\"1116976704\"\r\n}");
        Request request = new Request.Builder()
                .url("https://app.moutai519.com.cn/xhr/front/mall/reservation/add")
                .method("POST", body)
                .addHeader("MT-Lat", "31.15976")
                .addHeader("MT-Lng", "121.463072")
                .addHeader("MT-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTcyNjY0MzEyMSwidXNlcklkIjoxMTE2OTc2NzA0LCJkZXZpY2VJZCI6ImU5NjAxOGVlLTM5ZmEtNDY2NS05MTdjLWUyYWJlNjhkNGIzNyIsImlhdCI6MTcyNDA1MTEyMX0.MDUdgI4qbawWfob4I3_BXtivVAv_80DG2dyEAiwWjz8")
                .addHeader("MT-Info", "028e7f96f6369cafe1d105579c5b9377")
                .addHeader("MT-Device-ID", "e96018ee-39fa-4665-917c-e2abe68d4b37")
                .addHeader("MT-APP-Version", "1.7.1")
                .addHeader("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .addHeader("Content-Type", "application/json")
                .addHeader("userId", "1116976704")
                .build();
        Response response = client.newCall(request).execute();
        String res = new String(response.body().bytes(), "UTF-8");
        return "";
    }

    public static String post(String url, Map bodyContent, Headers headers)  {
        String res = "";
        try {
           OkHttpClient client = new OkHttpClient().newBuilder().build();
           MediaType mediaType = MediaType.parse("application/json");
           RequestBody body = RequestBody.create(mediaType, JSONObject.toJSONString(bodyContent));
           Request request = new Request.Builder()
                   .url(url)
                   .method("POST", body)
                   .headers(headers)
                   .build();
           Response response = client.newCall(request).execute();

           res = new String(response.body().bytes(), "UTF-8");
           System.out.println(res);
        } catch (Exception e) {
            throw new SecurityException(e.getMessage());
        }
        return res ;
    }
}
