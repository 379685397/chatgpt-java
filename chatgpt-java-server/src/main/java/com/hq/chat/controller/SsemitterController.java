package com.hq.chat.controller;

import com.hq.openai.api.OpenAiApi;
import com.hq.openai.core.OpenAiApiClient;
import com.hq.openai.model.completion.chat.ChatCompletionRequest;
import com.hq.openai.model.completion.chat.ChatMessage;
import com.hq.openai.model.completion.chat.ChatMessageRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sse/")
@Slf4j
public class SsemitterController {

        /**
         * 向SseEmitter对象发送数据
         * @return
         */
        @RequestMapping("/send")
        public SseEmitter setSseEmitter(String content) {
            SseEmitter sseEmitter = new SseEmitter(0L);
            sseEmitter.onCompletion(() -> {
                log.info("SSE已完成，关闭连接 id={}", content);
            });
            List<ChatMessage> messages = new ArrayList<ChatMessage>();
            final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
            messages.add(systemMessage);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo-0301")
                    .messages(messages)
                    .user("testing")
                    .maxTokens(1000)
                    .temperature(1.0)
                    .stream(true)
                    .build();

            OpenAiApi openApi = new OpenAiApiClient();
            openApi.createStreamChatCompletion(chatCompletionRequest,sseEmitter);
            return sseEmitter;
        }
    }
