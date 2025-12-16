package com.orishop.service;

import com.orishop.config.VnPayConfig;
import com.orishop.model.Order;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class VnPayServiceTest {

    private VnPayService vnPayService;

    private VnPayConfig vnPayConfig;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Use Real Config
        vnPayConfig = new VnPayConfig();
        vnPayConfig.vnp_TmnCode = "TESTCODE";
        vnPayConfig.vnp_HashSecret = "TESTSECRET";
        vnPayConfig.vnp_PayUrl = "http://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        vnPayConfig.vnp_ReturnUrl = "http://localhost:8080/return";
        
        // Setup Request Mock for IP Address
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        vnPayService = new VnPayService(vnPayConfig);
    }

    @Test
    public void testCreatePaymentUrl() {
        Order order = new Order();
        order.setId(12345L);
        order.setTotalAmount(new BigDecimal("100000")); // 100k VND
        
        String url = vnPayService.createPaymentUrl(order, request);
        
        System.out.println("Generated URL: " + url);
        
        assertNotNull(url);
        assertTrue(url.contains("vnp_TmnCode=TESTCODE"));
        assertTrue(url.contains("vnp_Amount=10000000")); // 100k * 100
        assertTrue(url.contains("vnp_TxnRef=12345"));
        assertTrue(url.contains("vnp_SecureHash="));
    }
}
