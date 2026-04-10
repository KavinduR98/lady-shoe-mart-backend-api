package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.ProductAdditionalImage;
import com.ushan.lady_shoe_mart.admin.domain.ProductImage;
import com.ushan.lady_shoe_mart.admin.domain.request.ProductSearchCriteriaRequest;
import com.ushan.lady_shoe_mart.admin.domain.response.PaginateShoeMartResponse;
import com.ushan.lady_shoe_mart.admin.entity.Brand;
import com.ushan.lady_shoe_mart.admin.entity.Category;
import com.ushan.lady_shoe_mart.admin.domain.Product;
import com.ushan.lady_shoe_mart.admin.entity.SubCategory;
import com.ushan.lady_shoe_mart.admin.entity.Supplier;
import com.ushan.lady_shoe_mart.admin.repository.*;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.common.util.ShoeMartConstant;
import com.ushan.lady_shoe_mart.common.util.enums.DiscountType;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Product> findAllProduct() {
        List<com.ushan.lady_shoe_mart.admin.entity.Product> productList = productRepository.findAllByIsActiveIsTrue();
        List<Product> productDomainList = new ArrayList<>();
        for (com.ushan.lady_shoe_mart.admin.entity.Product product : productList) {
            Product productDomain = new Product();
            modelMapper.map(product, productDomain);
            productDomain.setImage(productImageMapper(product.getImage()));
            productDomainList.add(productDomain);
        }
        return productDomainList;
    }

    @Override
    public PaginateShoeMartResponse<Product> productSearchCriteria(ProductSearchCriteriaRequest productSearchCriteriaRequest, String productSearch, Integer pageNo, Integer pageSize) {
        List<Product> products = productRepository.findAll((root, query, criteriaBuilder) -> {
            query.distinct(true);
            return productSearchCriteriaPredicate(root, criteriaBuilder, productSearch, productSearchCriteriaRequest);
        }, PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id", "dateCreated")))
                .stream()
                .map(this::productSearchMapper)
                .collect(Collectors.toList());

        long count = 10L;
        if (pageNo < 2) {
            count = productRepository.count((root, query, criteriaBuilder) -> {
                query.distinct(true);
                return productSearchCriteriaPredicate(root, criteriaBuilder, productSearch, productSearchCriteriaRequest);
            });
        }

        return new PaginateShoeMartResponse<Product>(products, count);
    }

    private Product productSearchMapper(com.ushan.lady_shoe_mart.admin.entity.Product product) {
        Product productDomain = new Product();

        productDomain.setId(product.getId());

        if (isNotNullOrEmpty(product.getProductNumber())) {
            productDomain.setProductNumber(product.getProductNumber());
        }
        if (product.getCategory() != null) {
            productDomain.setCategoryId(product.getCategory().getId());
            productDomain.setCategoryName(product.getCategory().getName());
            productDomain.setCategoryCode(product.getCategory().getCategoryCode());
        }
        if (product.getSubCategory() != null) {
            productDomain.setSubCategoryId(product.getSubCategory().getId());
            productDomain.setSubCategoryName(product.getSubCategory().getName());
        }
        if (product.getSupplier() != null) {
            productDomain.setSupplierId(product.getSupplier().getId());
            productDomain.setSupplierName(product.getSupplier().getName());
        }
        if (product.getIsActive() != null) {
            productDomain.setIsActive(product.getIsActive());
        }
        if (product.getDateCreated() != null) {
            productDomain.setDateCreated(product.getDateCreated());
        }
        if (product.getDateUpdated() != null) {
            productDomain.setDateUpdated(product.getDateUpdated());
        }
        if (isNotNullOrEmpty(product.getProductName())) {
            productDomain.setProductName(product.getProductName());
        }
        if (product.getStock() != null) {
            productDomain.setStock(product.getStock());
        }
        if (product.getActive() != null) {
            productDomain.setActive(product.getActive());
        }
        if (product.getNewArrival() != null) {
            productDomain.setNewArrival(product.getNewArrival());
        }
        if (product.getImage() != null) {
            productDomain.setImage(productImageMapper(product.getImage()));
        }
        if (product.getIsPromotional() != null) {
            productDomain.setIsPromotional(product.getIsPromotional());
        }
        if (product.getBrand() != null) {
            productDomain.setBrandId(Math.toIntExact(product.getBrand().getId()));
            productDomain.setBrandName(product.getBrand().getName());
        }
        if (isNotNullOrEmpty(product.getShortDescription())) {
            productDomain.setShortDescription(product.getShortDescription());
        }
        if (isNotNullOrEmpty(product.getLongDescription())) {
            productDomain.setLongDescription(product.getLongDescription());
        }
        if (isNotNullOrEmpty(product.getErpCode())) {
            productDomain.setErpCode(product.getErpCode());
        }
        if (product.getCostPrice() != null) {
            productDomain.setCostPrice(product.getCostPrice());
        }
        if (product.getSellingPriceLk() != null) {
            productDomain.setSellingPriceLk(product.getSellingPriceLk());
        }
        if (product.getMinOrderLevel() != null) {
            productDomain.setMinOrderLevel(product.getMinOrderLevel());
        }
        if (product.getMaxOrderLevel() != null) {
            productDomain.setMaxOrderLevel(product.getMaxOrderLevel());
        }
        if (product.getDiscountType() != null) {
            productDomain.setDiscountType(product.getDiscountType());
        }
        if (product.getDiscountedTimer() != null) {
            productDomain.setDiscountedTimer(product.getDiscountedTimer());
        }
        if (product.getDiscountStart() != null) {
            productDomain.setDiscountStart(product.getDiscountStart());
        }
        if (product.getDiscountEnd() != null) {
            productDomain.setDiscountEnd(product.getDiscountEnd());
        }
        if (product.getExpireDuration() != null) {
            productDomain.setExpireDuration(product.getExpireDuration());
        }
        if (product.getBestSeller() != null) {
            productDomain.setBestSeller(product.getBestSeller());
        }
        if (product.getOutOfSock() != null) {
            productDomain.setOutOfSock(product.getOutOfSock());
        } else {
            productDomain.setOutOfSock(true);
        }
        return productDomain;
    }

    private boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private Predicate productSearchCriteriaPredicate(Root<com.ushan.lady_shoe_mart.admin.entity.Product> root,
                                                 CriteriaBuilder criteriaBuilder, String searchString, ProductSearchCriteriaRequest productSearchCriteriaRequest) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(searchString)) {
            final String searchText = "%" + searchString.toLowerCase().trim() + "%";

            if (productSearchCriteriaRequest.getProductNumber() != null && productSearchCriteriaRequest.getProductNumber()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productNumber")), searchText));
            }
            if (productSearchCriteriaRequest.getErpCode() != null && productSearchCriteriaRequest.getErpCode()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("erpCode")), searchText));
            }
            if (productSearchCriteriaRequest.getProductName() != null && productSearchCriteriaRequest.getProductName()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), searchText));
            }
            if (productSearchCriteriaRequest.getCategory() != null && productSearchCriteriaRequest.getCategory()) {
                Join<com.ushan.lady_shoe_mart.admin.entity.Product, Category> joinCategory = root.join("category", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(joinCategory.get("name")), searchText));
            }
            if (productSearchCriteriaRequest.getSubCategory() != null && productSearchCriteriaRequest.getSubCategory()) {
                Join<com.ushan.lady_shoe_mart.admin.entity.Product, SubCategory> joinSubCategory = root.join("subCategory", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(joinSubCategory.get("name")), searchText));
            }
            if (productSearchCriteriaRequest.getBrand() != null && productSearchCriteriaRequest.getBrand()) {
                Join<com.ushan.lady_shoe_mart.admin.entity.Product, Brand> joinBrand = root.join("brand", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(joinBrand.get("name")), searchText));
            }
            if (productSearchCriteriaRequest.getSupplier() != null && productSearchCriteriaRequest.getSupplier()) {
                Join<com.ushan.lady_shoe_mart.admin.entity.Product, Supplier> joinSupplier = root.join("supplier", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(joinSupplier.get("name")), searchText));
            }
        }
        return criteriaBuilder.and(criteriaBuilder.or(predicates.toArray(new Predicate[]{})), criteriaBuilder.equal(root.get("isActive"), true));
    }

    public static ProductImage productImageMapper(ProductImage productImage) {
        ProductImage domainProductImage = new ProductImage();
        if (productImage != null) {
            if (Objects.nonNull(productImage.getMainImage()) && !productImage.getMainImage().contains(ShoeMartConstant.IMAGE_FOLDER_PRODUCT)) {
                domainProductImage.setMainImage(ShoeMartConstant.IMAGE_FOLDER_PRODUCT + "/" + productImage.getMainImage());
            }
            domainProductImage.setMainImageId(productImage.getMainImageId());
            domainProductImage.setSequence(productImage.getSequence());
            List<ProductAdditionalImage> productAdditionalImages = new ArrayList<>();
            if (productImage.getProductAdditionalImageList() != null && !productImage.getProductAdditionalImageList().isEmpty()) {
                for (ProductAdditionalImage productAdditionalImage : productImage.getProductAdditionalImageList()) {
                    ProductAdditionalImage domainProductAdditionalImage = getProductAdditionalImage(productAdditionalImage);
                    productAdditionalImages.add(domainProductAdditionalImage);
                }
            }
            domainProductImage.setProductAdditionalImageList(productAdditionalImages);
        }
        return domainProductImage;
    }

    public static ProductAdditionalImage getProductAdditionalImage(ProductAdditionalImage productAdditionalImage) {
        ProductAdditionalImage domainProductAdditionalImage = new ProductAdditionalImage();
        domainProductAdditionalImage.setId(productAdditionalImage.getId());
        if (Objects.nonNull(productAdditionalImage.getAdditionalImage()) && !productAdditionalImage.getAdditionalImage().contains(ShoeMartConstant.IMAGE_FOLDER_PRODUCT)) {
            domainProductAdditionalImage.setAdditionalImage(ShoeMartConstant.IMAGE_FOLDER_PRODUCT + "/" + productAdditionalImage.getAdditionalImage());
        }
        domainProductAdditionalImage.setSequence(productAdditionalImage.getSequence());
        return domainProductAdditionalImage;
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
