package com.hq.openai.utils;

import com.hq.openai.config.ErrorCodeConfig;
import com.hq.openai.constant.CommonError;
import com.hq.openai.model.http.ResultJson;

public class ResultUtils {
    public static ResultJson ResultOk() {
        return ResultOk(null);
    }

    public static ResultJson ResultOk(Object data) {
        ResultJson resultJson = ResultJson.builder()
                .code(CommonError.SUCCESS.getCode())
                .msg(CommonError.SUCCESS.getMsg()).build();
        if (data != null) {
            resultJson.setData(data);
        }
        return resultJson;
    }

    public static ResultJson ResultError() {
        return ResultError(CommonError.RETRY_ERROR);
    }

    public static ResultJson ResultError(String error) {
        CommonError commonError = ErrorCodeConfig.getInstance().getHttpCodeMap().get(error);
        return ResultError(commonError);
    }

    public static ResultJson ResultError(CommonError error) {
        ResultJson resultJson = null;
        if (error == null) {
            resultJson = ResultJson.builder()
                    .code(CommonError.SYS_ERROR.getCode())
                    .msg(CommonError.SYS_ERROR.getMsg()).build();
        } else {
            resultJson = ResultJson.builder()
                    .code(error.SYS_ERROR.getCode())
                    .msg(error.SYS_ERROR.getMsg()).build();
        }
        return resultJson;
    }
    public static boolean isSuccess(ResultJson result){
        if("200".equals(result.getCode())){
            return true;
        }else{
            return false;
        }

    }
}
