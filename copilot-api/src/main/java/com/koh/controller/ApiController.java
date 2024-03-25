package com.koh.controller;


import com.alibaba.fastjson.JSONObject;
import com.koh.service.TokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);


    private final WebClient webClient;

    @Resource
    private Map<String, TokenService> tokenServiceMap;

    @Autowired
    public ApiController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.githubcopilot.com").build();
    }

    @PostMapping(value = "/v1/chat/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamData(HttpServletRequest request,
                                   @RequestBody Map map) {
        String authorization = request.getHeader("Authorization");
        //通过split进行分组，根据空格，第一位是Bearer抛弃，第二位是渠道，第三位就是凭证
        String[] split = authorization.split(" ");
        TokenService tokenService = tokenServiceMap.get(split[1]);
        String token = tokenService.getToken(split[2]);
        Flux<String> jsonObjectFlux = fetchDataFromThirdParty(map,token);
        return jsonObjectFlux
                .filter(StringUtils::isNotEmpty)
                .map(json -> json.replaceFirst("data: ", ""));
    }

    public Flux<String> fetchDataFromThirdParty(Map map, String token) {
        return webClient.post()
                .uri("/chat/completions")
                .headers(headers -> {
                    headers.add("Authorization", "Bearer " + token);
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
