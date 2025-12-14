package com.orishop.repository;

import com.orishop.model.FlashSaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlashSaleProductRepository extends JpaRepository<FlashSaleProduct, Long> {
    List<FlashSaleProduct> findByFlashSaleId(Long flashSaleId);

    void deleteByFlashSaleId(Long flashSaleId);
}
