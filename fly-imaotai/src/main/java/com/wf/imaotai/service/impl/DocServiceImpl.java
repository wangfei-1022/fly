package com.wf.imaotai.service.impl;

import com.wf.imaotai.service.DocService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
public class DocServiceImpl implements DocService {
    @Override
    public int search() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "{\"encryKey\":\"f2a0ab415ab7b72142325aeacb35c5da454dc278648bef4212d0b47be4aeea4b\",\"data\":\"dbdd75c34fc530d971786fd1c11c594179ab75eb787aac822f4a5a34b24ecd25138e8dbdee4ba0c5914df475b5e44bdb50ca49320eefcb70c614c4efc6aac096449cb3b1776cf8912a18f68ee1dfa1c31a5e8bc41df1b476f640c11b1cf9bf58ce69ac5617d4855f45d42c98dde9a7ae\",\"sign\":\"96a62689cd3c164311664053bc0738b321629c5bfc120cfea163c0a8c7e73c2a\",\"timestamp\":1725519048000}");
        Request request = new Request.Builder()
                .url("https://wxgzh.fckyy.org.cn/api/hosservice/Appointment/GetPlanItem")
                .method("POST", body)
                .addHeader("Host", "wxgzh.fckyy.org.cn")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoib2JZUU90MUZZbzgxX0tJcTYydzhsV1oyeFozOCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IuWNgeeCueWNiuedoeiniSIsIl9UaW1lU3RhbXBfIjoiOS81LzIwMjQgMjo1MDozMiBQTSIsIm5iZiI6MTcyNTUxOTAzMiwiZXhwIjoxNzI1NjI3MDMyLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjQ0MzMyIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo0NDMzMiJ9.9OwELLN9wv4RBgRmhcvQCN_pVwS0mOCCO8uBlJboEig")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Origin", "https://wxgzh.fckyy.org.cn")
                .addHeader("Content-Length", "413")
                .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 17_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.42(0x18002a32) NetType/WIFI Language/zh_CN")
                .addHeader("Referer", "https://wxgzh.fckyy.org.cn/appointment/sourceDetail")
                .addHeader("Connection", "keep-alive")
                .addHeader("Sec-Fetch-Dest", "empty")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response);
        return 0;
    }

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, "{\"encryKey\":\"728fa3747ac37bb31d6cb46d63ab1d72e6a27e52afa7c58eaee62acabc60b2ce\",\"data\":\"dc4541ca6f91b7e30eff5412ccb0e4202fb51231be234a738f385cb2f2b52b01f050cc2474cd2001964cec03c9a2f537ae9c3fe2e24db6b6be886f2a984a86e7bd6340c068857434c6f28cd11d066f36f783fbd6e2cd9cc13fdd6ba3283f9b9764ce01495d20779179095ab0c3118a28\",\"sign\":\"ce7da9d6172fdb28275c79f7ff238748557d5ee248f01aa5fcc7df3f3744308d\",\"timestamp\":" + new Date().getTime() + "}");
        Request request = new Request.Builder()
                .url("https://wxgzh.fckyy.org.cn/api/hosservice/Appointment/GetPlanItem")
                .method("POST", body)
                .addHeader("Host", "wxgzh.fckyy.org.cn")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoib2JZUU90MUZZbzgxX0tJcTYydzhsV1oyeFozOCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IuWNgeeCueWNiuedoeiniSIsIl9UaW1lU3RhbXBfIjoiOS81LzIwMjQgNToyMDozOCBQTSIsIm5iZiI6MTcyNTUyODAzOCwiZXhwIjoxNzI1NjM2MDM4LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjQ0MzMyIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo0NDMzMiJ9.oIsnu8Fa6Eq-vAfMm2Ncg-WJZb4UoQmOmNctLcIPkeg")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Accept-Language", "zh-CN,zh-Hans;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Origin", "https://wxgzh.fckyy.org.cn")
                .addHeader("Content-Length", "413")
                .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 17_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.42(0x18002a32) NetType/WIFI Language/zh_CN")
                .addHeader("Referer", "https://wxgzh.fckyy.org.cn/appointment/sourceDetail")
                .addHeader("Connection", "keep-alive")
                .addHeader("Sec-Fetch-Dest", "empty")
                .build();
        Response response = client.newCall(request).execute();
        String res = new String(response.body().bytes(), "UTF-8");
        System.out.println(res);

        String xxx = "curl 'https://wxgzh.fckyy.org.cn/api/hosservice/Appointment/GetPlanItem'  -H 'Host: wxgzh.fckyy.org.cn'  -H 'Accept: application/json, text/plain, */*'  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoib2JZUU90MUZZbzgxX0tJcTYydzhsV1oyeFozOCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IuWNgeeCueWNiuedoeiniSIsIl9UaW1lU3RhbXBfIjoiOS81LzIwMjQgNToyNzo0NiBQTSIsIm5iZiI6MTcyNTUyODQ2NiwiZXhwIjoxNzI1NjM2NDY2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjQ0MzMyIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo0NDMzMiJ9.RN2NOf7DNst31V6Q-0KE_fYeMU90g7H53feP_7t_ngc'  -H 'Sec-Fetch-Site: same-origin'  -H 'Accept-Language: zh-CN,zh-Hans;q=0.9'  -H 'Accept-Encoding: gzip, deflate, br'  -H 'Sec-Fetch-Mode: cors'  -H 'Content-Type: application/json;charset=utf-8'  -H 'Origin: https://wxgzh.fckyy.org.cn'  -H 'Content-Length: 413'  -H 'User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 17_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.42(0x18002a32) NetType/4G Language/zh_CN'  -H 'Referer: https://wxgzh.fckyy.org.cn/appointment/sourceDetail'  -H 'Connection: keep-alive'  -H 'Sec-Fetch-Dest: empty'   --data '{\"encryKey\":\"e22329719470cf038d182727f3078616715eab3ce3d6ad5dfbbe0c6daf0f6dd8\",\"data\":\"d1e6999ba858e8e1bd78f4d70b76fa6f9a309fe13bb32a2875512082fc203a716c0ecb36713929ccae1501e55cbd28d932e02d57ad9540ab175d4ba06f6c2a268939e408db6532c0958689b8401f775d095ec22f2a9e01e904b9b180b20081dac36b8eb8ad5bca5a76590971f1309eda\",\"sign\":\"4f5d8e7383ce8da50c7c2b3f4bd17ee78dcee573d278f2171be6af3bf65c7133\",\"timestamp\":1725528498000}'";
        String yyy = "curl 'https://wxgzh.fckyy.org.cn/api/hosservice/Appointment/GetPlanItem'  -H 'Host: wxgzh.fckyy.org.cn'  -H 'Accept: application/json, text/plain, */*'  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoib2JZUU90MUZZbzgxX0tJcTYydzhsV1oyeFozOCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IuWNgeeCueWNiuedoeiniSIsIl9UaW1lU3RhbXBfIjoiOS81LzIwMjQgNToyNzo0NiBQTSIsIm5iZiI6MTcyNTUyODQ2NiwiZXhwIjoxNzI1NjM2NDY2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjQ0MzMyIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo0NDMzMiJ9.RN2NOf7DNst31V6Q-0KE_fYeMU90g7H53feP_7t_ngc'  -H 'Sec-Fetch-Site: same-origin'  -H 'Accept-Language: zh-CN,zh-Hans;q=0.9'  -H 'Accept-Encoding: gzip, deflate, br'  -H 'Sec-Fetch-Mode: cors'  -H 'Content-Type: application/json;charset=utf-8'  -H 'Origin: https://wxgzh.fckyy.org.cn'  -H 'Content-Length: 413'  -H 'User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 17_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.42(0x18002a32) NetType/4G Language/zh_CN'  -H 'Referer: https://wxgzh.fckyy.org.cn/appointment/sourceDetail'  -H 'Connection: keep-alive'  -H 'Sec-Fetch-Dest: empty'   --data '{\"encryKey\":\"dd9a44af9002b011b5c7dbdd39caf6f5f1e86b09d786f309be9eb415b90d07d9\",\"data\":\"33bd72be75ec1b83fc6bb4b1e52f8f3ea1f9a76b109c86a31a2a94bc33fd6ee1f7078fbb6439d1e2fef56b26249ff8d9eec1d066dce2fd144e9789222b5ef682da1cb194c45d57a0cf1fd3f9659ef417df1a748e854ec61ed04e6317f6dc05050e71d606a4e987a14773d3778d7d57db\",\"sign\":\"53d26ccf39523b63d0756fe8345c96d96a9eebdde9d2f1c2ff285d91be9ce13e\",\"timestamp\":1725528502000}'";
    }
}

