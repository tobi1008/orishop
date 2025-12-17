package com.orishop.service;

import com.orishop.config.MoMoConfig;
import com.orishop.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoMoService {

    private final MoMoConfig moMoConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public String createPayment(Order order) {
        String endpoint = moMoConfig.endpoint;
        String partnerCode = moMoConfig.partnerCode;
        String accessKey = moMoConfig.accessKey;
        String secretKey = moMoConfig.secretKey;
        String orderInfo = "Thanh toan don hang #" + order.getId();
        String redirectUrl = moMoConfig.returnUrl;
        String ipnUrl = moMoConfig.ipnUrl;
        String requestId = String.valueOf(UUID.randomUUID());
        String orderId = String.valueOf(order.getId());
        String amount = String.valueOf(order.getTotalAmount().longValue());
        String requestType = "captureWallet";
        String extraData = ""; // pass empty string if no extra data

        // rawSignature format: 
        // accessKey=$accessKey&amount=$amount&extraData=$extraData&ipnUrl=$ipnUrl&orderId=$orderId&orderInfo=$orderInfo&partnerCode=$partnerCode&redirectUrl=$redirectUrl&requestId=$requestId&requestType=$requestType
        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        String signature = moMoConfig.hmacSHA256(rawHash, secretKey);

        Map<String, String> map = new HashMap<>();
        map.put("partnerCode", partnerCode);
        map.put("partnerName", "OriShop");
        map.put("storeId", "MomoStore");
        map.put("requestId", requestId);
        map.put("amount", amount);
        map.put("orderId", orderId);
        map.put("orderInfo", orderInfo);
        map.put("redirectUrl", redirectUrl);
        map.put("ipnUrl", ipnUrl);
        map.put("lang", "vi");
        map.put("extraData", extraData);
        map.put("requestType", requestType);
        map.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

        try {
            // MoMo returns JSON containing payUrl
            ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, request, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("payUrl")) {
                return body.get("payUrl").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
