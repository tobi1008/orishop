package com.orishop.model;

public enum OrderStatus {
    PENDING, // Chờ xử lý
    CONFIRMED, // Đã xác nhận
    SHIPPING, // Đang giao
    DELIVERED, // Đã giao
    COMPLETED, // Hoàn thành (đã nhận hàng và không đổi trả)
    RETURN_REQUESTED, // Yêu cầu hoàn trả
    RETURNED, // Đã hoàn trả
    CANCELLED // Đã hủy
}
