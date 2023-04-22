package com.hq.openai.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiApiConfig {

    private String host = "XXXXX";
    private String key = "XXXXX";
    private String keyStrategy = "";
    private String tokenNum = "1000";

}
