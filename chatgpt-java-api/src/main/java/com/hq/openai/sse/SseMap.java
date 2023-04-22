package com.hq.openai.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseMap {
    public static final Map<String, Result> sseEmitterMap = new ConcurrentHashMap<>();
}
