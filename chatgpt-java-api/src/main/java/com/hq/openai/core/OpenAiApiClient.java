package com.hq.openai.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hq.openai.api.OpenAiApi;
import com.hq.openai.constant.ApiPathConstant;
import com.hq.openai.model.completion.chat.*;
import com.hq.openai.model.http.ResultJson;
import com.hq.openai.model.model.Model;
import com.hq.openai.sse.ConsoleEventSourceListener;
import com.hq.openai.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
public class OpenAiApiClient implements OpenAiApi {
    @Override
    public ResultJson completions(ChatCompletionRequest completion) {
        ResultJson result = OpenAiApiHttpClient.getInstance().post(ApiPathConstant.COMPLETIONS.CREATE_CHAT_COMPLETION,JSONObject.toJSONString(completion));
        if(ResultUtils.isSuccess(result)){
            ChatCompletionResult data = JSONObject.parseObject((String) result.getData(),ChatCompletionResult.class);
            return ResultUtils.ResultOk(data);
        }else{
            return result;
        }
    }

    @Override
    public ResultJson models() {
        ResultJson result = OpenAiApiHttpClient.getInstance().get(ApiPathConstant.MODEL.MODEL_LIST);
        if(ResultUtils.isSuccess(result)){
            JSONArray data = JSONObject.parseObject((String) result.getData()).getJSONArray("data");
            List<Model> modelList = data.toJavaList(Model.class);
            return ResultUtils.ResultOk(modelList);
        }else{
            return result;
        }
    }
    @Override
    public void createStreamChatCompletion(ChatCompletionRequest completion,SseEmitter sseEmitter) {
        ConsoleEventSourceListener listener = new ConsoleEventSourceListener(sseEmitter);
        if(completion.isStream()==false){
            completion.setStream(true);
        }
        OpenAiApiHttpClient.getInstance().streamChatCompletion(completion,listener);
    }
}
