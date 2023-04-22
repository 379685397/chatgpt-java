package com.hq.openai.sse;

import com.alibaba.fastjson.JSONObject;
import com.hq.openai.model.completion.chat.ChatMessage;
import com.hq.openai.model.completion.chat.ChatStreamCompletionChoice;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 描述： sse监听器
 */
@Slf4j
public class ConsoleEventSourceListener extends EventSourceListener {

    public SseEmitter sseEmitter;

    public SseEmitter getSseEmitter() {
        return sseEmitter;
    }

    public void setSseEmitter(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    public ConsoleEventSourceListener(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("OpenAI建立sse连接...");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.info("OpenAI返回数据：{}", data);
        if (data.equals("[DONE]")) {
            try {
//                ChatMessage context = new ChatMessage("system",data);
                sseEmitter.send(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("OpenAI返回数据结束了");
            return;
        }else{
            JSONObject result = JSONObject.parseObject(data);
            List<ChatStreamCompletionChoice> choices = result.getJSONArray("choices").toJavaList(ChatStreamCompletionChoice.class);
            System.out.println(choices.get(0).getDelta().getContent());
            ChatMessage context = new ChatMessage(choices.get(0).getDelta().getRole(), choices.get(0).getDelta().getContent());
            try {
                if(context!=null && context.getContent()!=null){
                    log.info("发送的数据：{}",context.getContent());
                    sseEmitter.send(JSONObject.toJSONString(context));






                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("OpenAI关闭sse连接...");
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if(Objects.isNull(response)){
            log.error("OpenAI  sse连接异常:{}", t);
            eventSource.cancel();
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }
}