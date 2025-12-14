package com.orishop.service.impl;

import com.orishop.model.FlashSale;
import com.orishop.model.FlashSaleProduct;
import com.orishop.model.Product;
import com.orishop.repository.FlashSaleProductRepository;
import com.orishop.repository.FlashSaleRepository;
import com.orishop.repository.ProductRepository;
import com.orishop.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlashSaleServiceImpl implements FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;
    private final FlashSaleProductRepository flashSaleProductRepository;
    private final ProductRepository productRepository;

    @Override
    public List<FlashSale> getAllFlashSales() {
        return flashSaleRepository.findAll();
    }

    @Override
    public FlashSale getFlashSaleById(Long id) {
        return flashSaleRepository.findById(id).orElse(null);
    }

    @Override
    public FlashSale saveFlashSale(FlashSale flashSale) {
        return flashSaleRepository.save(flashSale);
    }

    @Override
    public void deleteFlashSale(Long id) {
        flashSaleRepository.deleteById(id);
    }

    @Override
    public List<FlashSaleProduct> getProductsByFlashSaleId(Long flashSaleId) {
        return flashSaleProductRepository.findByFlashSaleId(flashSaleId);
    }

    @Override
    public void removeProductFromFlashSale(Long flashSaleProductId) {
        flashSaleProductRepository.deleteById(flashSaleProductId);
    }

    @Override
    public List<FlashSale> findActiveFlashSales(Date now) {
        return flashSaleRepository.findActiveFlashSales(now);
    }

    @Override
    @Transactional
    public void addProductToFlashSale(Long flashSaleId, Long productId, BigDecimal salePrice, String discountType,
            Double discountValue) {
        FlashSale flashSale = flashSaleRepository.findById(flashSaleId)
                .orElseThrow(() -> new RuntimeException("Flash Sale not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BigDecimal finalSalePrice = salePrice;

        if ("PERCENT".equals(discountType) && discountValue != null) {
            BigDecimal discount = product.getPrice().multiply(BigDecimal.valueOf(discountValue))
                    .divide(BigDecimal.valueOf(100));
            finalSalePrice = product.getPrice().subtract(discount);
        } else if (salePrice == null) {
            // Fallback if fixed price is chosen but not provided (should be handled by
            // frontend validation potentially)
            // For now, if no sale price and no percent, ignore or set to original price?
            // Let's assume frontend sends correct data.
            finalSalePrice = product.getPrice();
        }

        // Ensure price is not negative
        if (finalSalePrice.compareTo(BigDecimal.ZERO) < 0) {
            finalSalePrice = BigDecimal.ZERO;
        }

        // Check availability/update existing
        List<FlashSaleProduct> existing = flashSaleProductRepository.findByFlashSaleId(flashSaleId);
        for (FlashSaleProduct fsp : existing) {
            if (fsp.getProduct().getId().equals(productId)) {
                fsp.setSalePrice(finalSalePrice);
                flashSaleProductRepository.save(fsp);
                return;
            }
        }

        FlashSaleProduct newItem = new FlashSaleProduct();
        newItem.setFlashSale(flashSale);
        newItem.setProduct(product);
        newItem.setSalePrice(finalSalePrice);
        flashSaleProductRepository.save(newItem);
    }

    @Override
    @Transactional
    public void addCategoryToFlashSale(Long flashSaleId, Long categoryId, Double discountPercent) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        for (Product product : products) {
            // Apply percentage discount to all products
            addProductToFlashSale(flashSaleId, product.getId(), null, "PERCENT", discountPercent);
        }
    }

    @Override
    public Optional<BigDecimal> getFlashSalePrice(Product product) {
        List<FlashSale> activeSales = findActiveFlashSales(new Date());
        if (activeSales.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal lowestPrice = null;

        for (FlashSale sale : activeSales) {
            for (FlashSaleProduct fsp : sale.getFlashSaleProducts()) {
                if (fsp.getProduct().getId().equals(product.getId())) {
                    if (lowestPrice == null || fsp.getSalePrice().compareTo(lowestPrice) < 0) {
                        lowestPrice = fsp.getSalePrice();
                    }
                }
            }
        }

        return Optional.ofNullable(lowestPrice);
    }
}
