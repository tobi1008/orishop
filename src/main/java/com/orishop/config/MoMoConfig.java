package com.orishop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class MoMoConfig {

    @Value("${momo.partner-code}")
    public String partnerCode;

    @Value("${momo.access-key}")
    public String accessKey;

    @Value("${momo.secret-key}")
    public String secretKey;

    @Value("${momo.endpoint}")
    public String endpoint;

    @Value("${momo.return-url}")
    public String returnUrl;

    @Value("${momo.ipn-url}")
    public String ipnUrl;

    public String hmacSHA256(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
