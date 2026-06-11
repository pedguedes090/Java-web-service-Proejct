package com.example.project.service.impl;

import com.example.project.models.entity.User;
import com.example.project.service.JwtService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration-minutes}")
    private Long accessTokenExpirationMinutes;

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(accessTokenExpirationMinutes * 60);

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", user.getEmail());
        payload.put("userId", user.getId());
        payload.put("fullName", user.getFullName());
        payload.put("role", user.getRole().name());
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());

        try {
            String encodedHeader = base64Url(toJson(header).getBytes(StandardCharsets.UTF_8));
            String encodedPayload = base64Url(toJson(payload).getBytes(StandardCharsets.UTF_8));
            String content = encodedHeader + "." + encodedPayload;
            return content + "." + sign(content);
        } catch (Exception ex) {
            throw new IllegalStateException("Khong tao duoc JWT", ex);
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String content = parts[0] + "." + parts[1];
            if (!sign(content).equals(parts[2])) {
                return false;
            }

            Long expiresAt = getLongFromPayload(token, "exp");
            return expiresAt != null && expiresAt > Instant.now().getEpochSecond();
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return getStringFromPayload(token, "sub");
    }

    @Override
    public String getRoleFromToken(String token) {
        return getStringFromPayload(token, "role");
    }

    private String sign(String content) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(key);
        return base64Url(mac.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String getStringFromPayload(String token, String fieldName) {
        String payload = decodePayload(token);
        Matcher matcher = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"([^\"]*)\"").matcher(payload);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Long getLongFromPayload(String token, String fieldName) {
        String payload = decodePayload(token);
        Matcher matcher = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*(\\d+)").matcher(payload);
        return matcher.find() ? Long.parseLong(matcher.group(1)) : null;
    }

    private String decodePayload(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return "";
        }
        return new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
    }

    private String toJson(Map<String, Object> data) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(escape(entry.getKey())).append("\":");
            Object value = entry.getValue();
            if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else {
                json.append("\"").append(escape(String.valueOf(value))).append("\"");
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
