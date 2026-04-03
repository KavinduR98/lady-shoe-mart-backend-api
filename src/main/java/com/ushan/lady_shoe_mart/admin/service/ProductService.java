package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.entity.Brand;
import com.ushan.lady_shoe_mart.admin.entity.Category;
import com.ushan.lady_shoe_mart.admin.domain.Product;
import com.ushan.lady_shoe_mart.admin.entity.SubCategory;
import com.ushan.lady_shoe_mart.admin.entity.Supplier;
import com.ushan.lady_shoe_mart.admin.repository.*;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.common.util.enums.DiscountType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService{

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<Product> save(Product product) {
        ApiResponse<Product> response = new ApiResponse<>();
        com.ushan.lady_shoe_mart.admin.entity.Product productEntity = new com.ushan.lady_shoe_mart.admin.entity.Product();

        if (product.getCategoryId() == null) {
            throw new LsmException("Category can't be empty");
        }
        Optional<Category> categoryOptional = categoryRepository.findById(product.getCategoryId());
        if (categoryOptional.isEmpty()) {
            throw new LsmException("Category not found!");
        }
        productEntity.setCategory(categoryOptional.get());
        if (product.getSubCategoryId() != null) {
            Optional<SubCategory> subCategoryOptional = subcategoryRepository.findById(product.getSubCategoryId());
            if (subCategoryOptional.isEmpty()) {
                throw new LsmException("Sub category not found!");
            }
            productEntity.setSubCategory(subCategoryOptional.get());
        }
        Optional<Supplier> supplierOptional = supplierRepository.findById(product.getSupplierId());
        if (supplierOptional.isEmpty()) {
            throw new LsmException("Supplier not found!");
        }
        productEntity.setSupplier(supplierOptional.get());
        if (product.getProductName() == null) {
            throw new LsmException("Product name can't be empty");
        }
        productEntity.setProductName(product.getProductName());
        String productNumber = generateProductNumber(categoryOptional.get().getCategoryCode(), supplierOptional.get().getSupplierId());
        productEntity.setProductNumber(productNumber);
        productEntity.setProductName(product.getProductName());
        if (product.getActive() == null) {
            productEntity.setActive(false);
        } else {
            productEntity.setActive(product.getActive());
        }
        if (product.getStock() == null) {
            productEntity.setStock(0.0);
        } else {
            if (product.getStock() < 0) {
                throw new LsmException("Stock can't be negative");
            }
            productEntity.setStock(product.getStock());
        }
        productEntity.setActive(Boolean.TRUE);
        if (Objects.isNull(product.getDiscountType())) {
            productEntity.setDiscountType(DiscountType.PERCENTAGE);
            productEntity.setDiscountedPrice(0.00);
        } else {
            productEntity.setDiscountType(product.getDiscountType());
            productEntity.setDiscountedPrice(Objects.isNull(product.getDiscountedPrice()) ? 0 : product.getDiscountedPrice());
        }
        productEntity.setNewArrival(product.getNewArrival());
        productEntity.setIsPromotional(product.getIsPromotional());
        productEntity.setChildProduct(product.getChildProduct());
        if (product.getBrandId() == null) {
            throw new LsmException("Brand id can't be empty!");
        }
        Optional<Brand> brandOptional = brandRepository.findById(product.getBrandId().longValue());
        if (brandOptional.isEmpty()) {
            throw new LsmException("Brand not found!");
        }
        productEntity.setBrand(brandOptional.get());
        productEntity.setShortDescription(product.getShortDescription());
        productEntity.setLongDescription(product.getLongDescription());
        if (product.getErpCode() != null) {
            if (productRepository.existsProductByErpCodeIgnoreCase(product.getErpCode())) {
                throw new LsmException("Erp Code already exist!");
            }
            productEntity.setErpCode(product.getErpCode());
        }
        productEntity.setOutOfSock(product.getStock() == 0);
        if (product.getCostPrice() != null) {
            if (product.getCostPrice() < 0) {
                throw new LsmException("Product cost price can't be negative!");
            }
            productEntity.setCostPrice(product.getCostPrice());
        }
        if (product.getSellingPriceLk() != null) {
            if (product.getSellingPriceLk() <= 0) {
                throw new LsmException("Product selling LK price can't be negative or zero!");
            }
            productEntity.setSellingPriceLk(product.getSellingPriceLk());
        }
        productEntity.setMinOrderLevel(product.getMinOrderLevel());
        productEntity.setMaxOrderLevel(product.getMaxOrderLevel());
        productEntity.setDiscountedTimer(product.getDiscountedTimer());
        productEntity.setDiscountStart(product.getDiscountStart());
        productEntity.setDiscountEnd(product.getDiscountEnd());
        productEntity.setExpireDuration(product.getExpireDuration());

        if (Objects.nonNull(product.getBestSeller())) {
            productEntity.setBestSeller(product.getBestSeller());
        }
        checkDiscountValue(productEntity);
        productRepository.save(productEntity);

        product = modelMapper.map(productEntity, Product.class);
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Product is successfully saved");
        response.setObject(product);
        return response;
    }

    private void checkDiscountValue(com.ushan.lady_shoe_mart.admin.entity.Product product) {
        if (Objects.equals(product.getDiscountType(), DiscountType.VALUE)) {
            if ((product.getDiscountedPrice() != null) &&
                (product.getDiscountedPrice() > (product.getSellingPriceLk() == null ? 0 : product.getSellingPriceLk()))) {
                throw new LsmException("Discount Price Is More Than Product Price");
            }
        } else if (Objects.equals(product.getDiscountType(), DiscountType.PERCENTAGE)) {
            if ((product.getDiscountedPrice() != null) && (product.getDiscountedPrice() > 100)) {
                throw new LsmException("Discount Price Is More Than 100");
            }
        }
    }

    private String generateProductNumber(String categoryCode, String supplierId) {
        String prefix = (categoryCode.trim() + supplierId.trim()).toUpperCase();
        int maxSuffix = productRepository.findMaxProductNumberSuffix(prefix, prefix.length());
        int nextNumber = maxSuffix + 1;
        return prefix + nextNumber;  // e.g. "HEELSNIKE4"
    }
}
