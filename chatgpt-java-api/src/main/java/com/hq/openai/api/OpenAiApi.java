package com.hq.openai.api;

import com.hq.openai.model.completion.chat.ChatCompletionRequest;
import com.hq.openai.model.http.ResultJson;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface OpenAiApi {

    /**
     * 文本问答
     * Given a prompt, the model will return one or more predicted completions,
     * and can also return the probabilities of alternative tokens at each position.
     * @param completion
     * @return Single CompletionResponse
     */
    ResultJson completions(ChatCompletionRequest completion);
    /**
     * 模型列表
     *  url path = v1/models
     * @return Single ModelResponse
     */
    ResultJson models();

    void createStreamChatCompletion(ChatCompletionRequest completion, SseEmitter sseEmitter);

}
