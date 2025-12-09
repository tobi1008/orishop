package com.orishop.service.impl;

import com.orishop.model.Coupon;
import com.orishop.repository.CouponRepository;
import com.orishop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    @Override
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code).orElse(null);
    }

    @Override
    public boolean isCouponValid(String code) {
        Optional<Coupon> couponOpt = couponRepository.findByCode(code);
        if (couponOpt.isEmpty()) {
            return false;
        }

        Coupon coupon = couponOpt.get();
        Date now = new Date();

        // Check dates
        if (coupon.getStartDate() != null && now.before(coupon.getStartDate())) {
            return false;
        }
        if (coupon.getEndDate() != null && now.after(coupon.getEndDate())) {
            return false;
        }

        // Check usage limit
        if (coupon.getUsageLimit() != null && coupon.getUsedCount() != null
                && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            return false;
        }

        return true;
    }
}
