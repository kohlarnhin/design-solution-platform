package com.koh.service.impl;

import com.koh.service.TokenService;
import org.apache.el.parser.Token;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 淘宝获取token
 */
@Service(value = "TaoBao")
public class TaoBaoTokenServiceImpl implements TokenService {
    @Override
    public String getToken(String key) {
        String token = null;
        try {
            // 设置GET请求URL
            URL url = new URL("http://highcopilot.micosoft.icu/copilot_internal/v2/token");

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 添加Authorization头部
            connection.setRequestProperty("Authorization", "token " + key);

            // 获取响应代码
            int responseCode = connection.getResponseCode();

            // 读取响应
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // 将响应转换为字符串
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 关闭连接
            connection.disconnect();

            // 提取token
            token = extractTokenFromResponse(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    // 从JSON响应中提取token
    private static String extractTokenFromResponse(String jsonResponse) {
        // 在实际应用中，最好使用JSON解析库，例如Jackson或Gson
        // 这里为了简单起见，使用字符串处理
        String token = null;
        String[] parts = jsonResponse.split("\"token\":");
        if (parts.length > 1) {
            String[] tokenParts = parts[1].split("\"");
            if (tokenParts.length > 1) {
                token = tokenParts[1];
            }
        }
        return token;
    }
}
