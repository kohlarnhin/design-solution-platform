package com.koh.controller;


import com.alibaba.fastjson.JSONObject;
import com.koh.service.TokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.UUID;

@RestController
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);


    private final WebClient webClient;

    // 初始化 session 相关变量
    private final String vscodeMachineId;
    private long lastSessionIdTime = 0;
    private long updateSessionIdTime;
    private String githubToken;

    @Resource
    private Map<String, TokenService> tokenServiceMap;

    @Autowired
    public ApiController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.githubcopilot.com").build();
        this.vscodeMachineId = generateVscodeMachineId();
        this.updateSessionIdTime = genSessionIdUpdateTime();
    }

    @PostMapping(value = "/v1/chat/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamData(HttpServletRequest request,
                                   @RequestBody Map map) {
        String authorization = request.getHeader("Authorization");
        //通过split进行分组，根据空格，第一位是Bearer抛弃，第二位是渠道，第三位就是凭证
        String[] split = authorization.split(" ");
        TokenService tokenService = tokenServiceMap.get(split[1]);
        githubToken = split[2];
        String token = tokenService.getToken(split[2]);
        Flux<String> jsonObjectFlux = fetchDataFromThirdParty(map,token);
        return jsonObjectFlux
                .filter(StringUtils::isNotEmpty)
                .map(json -> json.replaceFirst("data: ", ""));
    }

    public Flux<String> fetchDataFromThirdParty(Map map, String token) {
        String requestId = generateRequestId();
        String vscodeSessionId = generateVscodeSessionId();
        logger.info("\n打印测试常量变量\n requestId: {}\n, vscodeSessionId:{}\n, vscodeMachineId:{}\n",
                requestId,vscodeSessionId,vscodeMachineId);
        return webClient.post()
                .uri("/chat/completions")
                .headers(headers -> {
                    headers.add("Host", "api.githubcopilot.com");
                    headers.add("X-Request-Id", requestId);
                    headers.add("Vscode-Sessionid", vscodeSessionId);
                    headers.add("Vscode-Machineid", vscodeMachineId);
                    headers.add("X-Github-Api-Version", "2023-07-07");
                    headers.add("Editor-Version", "vscode/1.86.2");
                    headers.add("Editor-Plugin-Version", "copilot-chat/0.12.2");
                    headers.add("Openai-Organization", "github-copilot");
                    headers.add("Copilot-Integration-Id", "vscode-chat");
                    headers.add("Openai-Intent", "conversation-panel");
                    headers.add("Content-Type", "application/json");
                    headers.add("User-Agent", "GitHubCopilotChat/0.12.2");
                    headers.add("Accept", "*/*");
                    headers.add("Accept-Encoding", "gzip,deflate,br");
                    headers.add("Connection", "keep-alive");
                    headers.add("Authorization", "Bearer " + token);
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

    // 生成 vscode_machine_id
    private String generateVscodeMachineId() {
        String salt = "20240311"; // 替换成你的 salt
        String combinedString = githubToken + salt;
        return DigestUtils.sha256Hex(combinedString);
    }

    // 生成 session_id 更新时间
    private long genSessionIdUpdateTime() {
        // 根据 Python 代码的逻辑生成 session_id 更新时间
        // 这里以示例值替代
        // 示例值表示 1-90 分钟随机更新一次
        return (long) (Math.random() * 5400) + 60; // 60 秒到 90 分钟
    }

    // 生成 request_id
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    // 生成 vscode_session_id
    private String generateVscodeSessionId() {
        long now = System.currentTimeMillis() / 1000;
        if (now - lastSessionIdTime > updateSessionIdTime) {
            lastSessionIdTime = now;
            updateSessionIdTime = genSessionIdUpdateTime();
        }
        return UUID.randomUUID().toString() + now;
    }

}
