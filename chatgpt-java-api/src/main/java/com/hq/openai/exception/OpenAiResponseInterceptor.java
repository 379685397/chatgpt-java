package com.hq.openai.exception;//package com.hq.openai.exception;
//
//import cn.hutool.json.JSONUtil;
//import com.hq.openai.config.ErrorCodeConfig;
//import com.hq.openai.constant.CommonError;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.IOException;
//import java.util.Objects;
//
///**
// * 描述：openai 返回值处理Interceptor
// * @author Teacher Wang
// * @since  2023-03-23
// */
//@Slf4j
//public class OpenAiResponseInterceptor implements Interceptor {
//
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//
//        Request original = chain.request();
//        Response response = chain.proceed(original);
//        String HttpCode = String.valueOf(response.code());
//        if (!response.isSuccessful()) {
//            String errorMsg = ErrorCodeConfig.getInstance().getHttpCodeMap().get(CommonError.OPENAI_SERVER_ERROR.getCode());
//            if(StringUtils.isNotBlank(errorMsg)){
//                log.error(errorMsg);
//            }else{
//                log.error(CommonError.RETRY_ERROR.getMsg());
//            }
//        }
//        return response;
//    }
//}
