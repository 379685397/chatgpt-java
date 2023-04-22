package com.hq.openai.sse;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin
@RestController
@RequestMapping("/web/sse")
@Slf4j
public class SsemitterController {

        /**
         * 返回SseEmitter对象
         *
         * @param clientId
         * @return
         */
        @RequestMapping("/start")
        public SseEmitter testSseEmitter(String clientId) {
            // 默认30秒超时,设置为0L则永不超时
            SseEmitter sseEmitter = new SseEmitter(0L);
            sseEmitter.onCompletion(() -> {
                log.info("SSE已完成，关闭连接 id={}", clientId);
                SseMap.sseEmitterMap.remove(clientId);
            });
            SseMap.sseEmitterMap.put(clientId, new Result(clientId, System.currentTimeMillis(), sseEmitter));
            return sseEmitter;
        }

        /**
         * 向SseEmitter对象发送数据
         *
         * @return
         */
        @RequestMapping("/send")
        public SseEmitter setSseEmitter(String content) {
//                Result result =  SseMap.sseEmitterMap.get(clientId);
//                if(result == null){
                    // 默认30秒超时,设置为0L则永不超时
                    SseEmitter sseEmitter = new SseEmitter(0L);
                    sseEmitter.onCompletion(() -> {
                        log.info("SSE已完成，关闭连接");
//                        SseMap.sseEmitterMap.remove(clientId);
                    });
              Result result =  new Result(content, System.currentTimeMillis(), sseEmitter);
                    SseMap.sseEmitterMap.put(content, result);
//                }
                if (result != null && result.sseEmitter != null) {
                    ConsoleEventSourceListener listener = new ConsoleEventSourceListener(result.sseEmitter);
                }
            return result.getSseEmitter();
        }

        /**
         * 将SseEmitter对象设置成完成
         *
         * @param clientId
         * @return
         */
        @RequestMapping("/end")
        public String completeSseEmitter(String clientId) {
            Result result =  SseMap.sseEmitterMap.get(clientId);
            if (result != null) {
                SseMap.sseEmitterMap.remove(clientId);
                result.sseEmitter.complete();
            }
            return "Succeed!";
        }

    }
