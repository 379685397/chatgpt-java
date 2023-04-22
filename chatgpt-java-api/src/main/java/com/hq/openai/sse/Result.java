package com.hq.openai.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    public String clientId;
    public long timestamp;
    public SseEmitter sseEmitter;

}
