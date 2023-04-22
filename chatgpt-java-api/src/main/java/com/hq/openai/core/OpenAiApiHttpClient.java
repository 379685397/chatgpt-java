package com.hq.openai.core;


import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hq.openai.Interceptor.HeaderAuthorizationInterceptor;
import com.hq.openai.config.OpenAiApiConfig;
import com.hq.openai.constant.ApiPathConstant;
import com.hq.openai.constant.CommonError;
import com.hq.openai.model.completion.chat.ChatCompletionRequest;
import com.hq.openai.model.http.ResultJson;
import com.hq.openai.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OpenAiApiHttpClient {
    private static OpenAiApiConfig openAiApiConfig;
    private static OpenAiApiHttpClient openAiApiClient;
    private static OkHttpClient okHttpClient;

    public OpenAiApiHttpClient(OpenAiApiConfig openAiApiConfig) {
        this.openAiApiConfig = openAiApiConfig;
    }

    public static OpenAiApiHttpClient getInstance() {
        if (openAiApiClient == null) {
            init();
        }
        return openAiApiClient;
    }

    public static void init() {
        openAiApiConfig = new OpenAiApiConfig();
        openAiApiClient = new OpenAiApiHttpClient(openAiApiConfig);
        openAiApiClient.okHttpClient = okHttpClient();

    }

    private static OkHttpClient okHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(new HeaderAuthorizationInterceptor(openAiApiConfig.getKey()))
//                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        return okHttpClient;
    }

    /**
     * 无请求头
     *
     * @param url
     * @return
     * @throws IOException
     */
    public ResultJson get(String url) {
        Request request = new Request.Builder().url(openAiApiConfig.getHost()+url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("接口请求成功，body is {}", result);
                return ResultUtils.ResultOk(result);
            }else{
                return ResultUtils.ResultError(String.valueOf(response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.ResultError(CommonError.SYS_ERROR);
    }

    /**
     * 有请求头
     *
     * @param url
     * @return
     */
    public ResultJson get(String url, Map<String, Object> headersMap) {
        Request.Builder request = new Request.Builder()
                .url(openAiApiConfig.getHost()+url);
        for (Map.Entry<String, Object> header : headersMap.entrySet()) {
            request.addHeader(header.getKey(), String.valueOf(header.getValue()));
        }
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("接口请求成功，body is {}", result);
                return ResultUtils.ResultOk(result);
            }else{
                return ResultUtils.ResultError(String.valueOf(response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.ResultError(CommonError.SYS_ERROR);
    }

    /**
     * Post
     *
     * @return
     */
    public ResultJson post(String path) {
        return post(path, null);
    }

    /**
     * Post
     *
     * @return
     */
    public ResultJson post(String path, String requestBody) {
        if (StringUtils.isBlank(requestBody)) {
            requestBody = "";
        }
        Request request = new Request.Builder()
                .url(openAiApiConfig.getHost() + path)
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("接口请求成功，body is {}", result);
                return ResultUtils.ResultOk(result);
            }else{
                return ResultUtils.ResultError(String.valueOf(response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.ResultError(CommonError.SYS_ERROR);
    }

    /**
     * Post
     *
     * @return
     */
    public ResultJson post(String url, String requestBody, Map<String, Object> headersMap) {
        Request.Builder request = new Request.Builder()
                .url(openAiApiConfig.getHost()+url)
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody));
        for (Map.Entry<String, Object> header : headersMap.entrySet()) {
            request.addHeader(header.getKey(), String.valueOf(header.getValue()));
        }
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("接口请求成功，body is {}", result);
                return ResultUtils.ResultOk(result);
            }else{
                return ResultUtils.ResultError(String.valueOf(response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.ResultError(CommonError.SYS_ERROR);
    }

    public void streamChatCompletion(ChatCompletionRequest chatCompletion, EventSourceListener eventSourceListener) {

        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
        }
        try {
            EventSource.Factory factory = EventSources.createFactory(okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);
            Request request = new Request.Builder()
                    .url(openAiApiConfig.getHost()+ ApiPathConstant.COMPLETIONS.CREATE_CHAT_COMPLETION)
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                    .build();
            //创建事件
            EventSource eventSource = factory.newEventSource(request, eventSourceListener);
        } catch (JsonProcessingException e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        }
    }

}
