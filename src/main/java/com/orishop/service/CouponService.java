package com.orishop.service;

import com.orishop.model.Coupon;
import java.util.List;
import java.util.Optional;

public interface CouponService {
    List<Coupon> getAllCoupons();

    Coupon saveCoupon(Coupon coupon);

    void deleteCoupon(Long id);

    Coupon getCouponById(Long id);

    Coupon getCouponByCode(String code);

    boolean isCouponValid(String code);
}
