package com.hq.openai.Interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import lombok.Getter;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 描述：请求增加header apikey
 * @author Teacher Wang
 * @since 2023-04-20
 */
@Getter
public class HeaderAuthorizationInterceptor implements Interceptor {

    private String apiKey;

    /**
     * 请求头处理
     * @param apiKey        api keys列表
     */
    public HeaderAuthorizationInterceptor(String apiKey) {
        this.apiKey = apiKey;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
