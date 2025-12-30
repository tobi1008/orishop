package com.orishop.model;

public enum OrderStatus {
    PENDING, // Chờ xử lý
    CONFIRMED, // Đã xác nhận
    SHIPPING, // Đang giao
    COMPLETED, // Hoàn thành (đã nhận hàng và không đổi trả)
    RETURN_REQUESTED, // Yêu cầu hoàn trả (hàng)
    REFUND_REQUESTED, // Yêu cầu hoàn tiền (khi hủy đơn đã thanh toán)
    RETURNED, // Đã hoàn trả
    CANCELLED // Đã hủy
}
