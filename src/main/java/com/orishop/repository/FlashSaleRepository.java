package com.orishop.repository;

import com.orishop.model.FlashSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Long> {

    @Query("SELECT fs FROM FlashSale fs WHERE fs.status = true AND fs.startTime <= :now AND fs.endTime >= :now")
    List<FlashSale> findActiveFlashSales(Date now);
}
