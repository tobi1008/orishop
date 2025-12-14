package com.orishop.service;

import com.orishop.model.FlashSale;
import com.orishop.model.FlashSaleProduct;
import com.orishop.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.Optional;

public interface FlashSaleService {
    List<FlashSale> getAllFlashSales();

    FlashSale getFlashSaleById(Long id);

    FlashSale saveFlashSale(FlashSale flashSale);

    void deleteFlashSale(Long id);

    // Manage products in sale
    List<FlashSaleProduct> getProductsByFlashSaleId(Long flashSaleId);

    // Updated to handle discount logic
    void addProductToFlashSale(Long flashSaleId, Long productId, java.math.BigDecimal salePrice, String discountType,
            Double discountValue);

    void removeProductFromFlashSale(Long flashSaleProductId);

    // For User
    List<FlashSale> findActiveFlashSales(Date now);

    // Check active flash sale price for a product
    java.util.Optional<java.math.BigDecimal> getFlashSalePrice(Product product);

    // New method for bulk add by category
    void addCategoryToFlashSale(Long flashSaleId, Long categoryId, Double discountPercent);
}
