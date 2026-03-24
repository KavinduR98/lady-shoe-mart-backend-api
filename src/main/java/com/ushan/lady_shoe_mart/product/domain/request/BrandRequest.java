package com.ushan.lady_shoe_mart.product.domain.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class BrandRequest {
    private MultipartFile image;
    private String brandCode;
    private String name;
    private Integer indexSeq;
    private Boolean active;
    private Boolean isActive;
}
