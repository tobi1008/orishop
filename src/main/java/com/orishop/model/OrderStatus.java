package com.orishop.model;

public enum OrderStatus {
    PENDING, // Chờ xử lý
    CONFIRMED, // Đã xác nhận
    SHIPPING, // Đang giao
    DELIVERED, // Đã giao
    COMPLETED, // Hoàn thành (đã nhận hàng và không đổi trả)
    CANCELLED // Đã hủy
}
