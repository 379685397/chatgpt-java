package com.hq.openai.constant;

public class ApiPathConstant {

    public static class MODEL {
        public static String MODEL_LIST = "v1/models";
    }

    public static class COMPLETIONS {
        public static String CREATE_COMPLETION = "v1/completions";
        public static String CREATE_CHAT_COMPLETION = "v1/chat/completions";
        public static String CREATE_COMPLETION_id = "v1/engines/{engine_id}/completions";
    }
}
