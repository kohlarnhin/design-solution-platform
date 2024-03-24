package com.koh.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);


    private final WebClient webClient;

    @Autowired
    public ApiController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.githubcopilot.com").build();
    }


//    @PostMapping("/v1/chat/completions")
//    public void test() {
//        System.out.println("111");
//    }

    @PostMapping(value = "/v1/chat/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamData(@RequestBody Map map) {
        // 从第三方接口获取数据流
        Flux<String> dataStream = callThirdPartyApi(map);

        // 返回数据流
        return dataStream;
    }

    public Flux<String> callThirdPartyApi(Map map) {
        Flux<String> jsonObjectFlux = fetchDataFromThirdParty(map);
        Flux<String> map1 = jsonObjectFlux.map(json -> {
            String s = extractContentFromResponse(json);
            return s;
        });
        return map1;
    }

    private static String extractContentFromResponse(String jsonResponse) {
        if (StringUtils.isNotEmpty(jsonResponse)) {
            String replace = jsonResponse.replaceFirst("data: ", "");
            JSONObject jsonObject = JSON.parseObject(replace);
            JSONArray choices = jsonObject.getJSONArray("choices");
            if (!CollectionUtils.isEmpty(choices)) {
                Long created = jsonObject.getLong("created");
                String id = jsonObject.getString("id");
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("object", "chat.completion.chunk");
                map.put("created", created);
                map.put("model", "gpt-4");
                map.put("choices", choices);
                return JSONObject.toJSONString(map);
            }
        }
        return "";
    }

    public Flux<String> fetchDataFromThirdParty(Map map) {
        return webClient.post()
                .uri("/chat/completions")
                .headers(headers -> {
                    headers.add("Authorization", "Bearer " + "tid=f57d0f3b622672e64d0318b7faab5709;exp=1711303970;sku=trial_30_monthly_subscriber;st=dotcom;chat=1;8kp=1:651646d3e58aa155a7d4164cd992b1ad367c5acbe92f76cdbc67085727b65b31");
                    headers.add("Editor-Version", "vscode/1.86.2");
                })
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSONObject.toJSONString(map)) // 设置请求体
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    String res = ex.getResponseBodyAsString();
                    return Mono.error(new RuntimeException(res));
                });

    }
}
