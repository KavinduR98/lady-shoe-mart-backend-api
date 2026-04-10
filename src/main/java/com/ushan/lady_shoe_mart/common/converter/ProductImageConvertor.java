package com.ushan.lady_shoe_mart.common.converter;

import com.google.gson.Gson;
import com.ushan.lady_shoe_mart.admin.domain.ProductImage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProductImageConvertor implements AttributeConverter<ProductImage, String> {
    @Override
    public String convertToDatabaseColumn(ProductImage productImage) {
        return productImage != null ? new Gson().toJson(productImage) : null;
    }

    @Override
    public ProductImage convertToEntityAttribute(String productImage) {
        return productImage != null && !productImage.equals("null") ? new Gson().fromJson(productImage, ProductImage.class) : null;
    }
}
