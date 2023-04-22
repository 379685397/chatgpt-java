package com.hq.openai.config;

import com.hq.openai.constant.CommonError;

import java.util.HashMap;

public class ErrorCodeConfig {
    private static ErrorCodeConfig errorCodeConfig;
    public static HashMap<String, CommonError> httpCodeMap;

    public ErrorCodeConfig() {

    }

    public static ErrorCodeConfig getInstance() {
        if (errorCodeConfig == null) {
            init();
        }
        return errorCodeConfig;
    }

    //初始化错误码
    public static void init() {
        errorCodeConfig = new ErrorCodeConfig();
        httpCodeMap = new HashMap<>();
        CommonError values[] = CommonError.values();
        for (CommonError commonErrors : values) {
            httpCodeMap.put(commonErrors.getCode(),commonErrors);
        }

    }

    public  HashMap<String, CommonError> getHttpCodeMap() {
        return httpCodeMap;
    }

}
