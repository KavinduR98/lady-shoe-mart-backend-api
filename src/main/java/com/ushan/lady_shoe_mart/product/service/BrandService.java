package com.ushan.lady_shoe_mart.product.service;

import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.common.util.ShoeMartConstant;
import com.ushan.lady_shoe_mart.product.domain.Brand;
import com.ushan.lady_shoe_mart.product.domain.request.BrandRequest;
import com.ushan.lady_shoe_mart.product.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BrandService implements IBrandService{

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IImageService imageService;

    @Transactional
    @Override
    public ApiResponse<Brand> save(BrandRequest brand) {
        ApiResponse<Brand> response = new ApiResponse<>();
        if (brand.getImage() == null) {
            throw new LsmException("Image can't be empty");
        } else if (brand.getName() == null || brand.getName().isEmpty()) {
            throw new LsmException("Brand name can't be empty");
        } else if (brand.getActive() == null) {
            throw new LsmException("Active status can't be empty");
        } else if (Objects.isNull(brand.getBrandCode()) || brand.getBrandCode().isEmpty() || brand.getBrandCode().length() > 20) {
            throw new LsmException("Brand Code can't be empty OR Code length can't be more than 20");
        } else {
            brandCodeIsExist(brand.getBrandCode());
        }
        com.ushan.lady_shoe_mart.product.entity.Brand brandEntity = new com.ushan.lady_shoe_mart.product.entity.Brand();
        brandEntity.setImage(imageService.uploadFile(brand.getImage(), ShoeMartConstant.IMAGE_FOLDER_BRAND, ShoeMartConstant.IMAGE_PREFIX_BRAND));
        brandEntity.setName(brand.getName());
        brandEntity.setBrandCode(brand.getBrandCode());
        brandEntity.setIndexSeq(brand.getIndexSeq() == null || brand.getIndexSeq() < 1 ? 100000 : brand.getIndexSeq());
        brandEntity.setIsActive(Boolean.TRUE);
        brandEntity.setActive(Boolean.TRUE);
        brandEntity.setDateCreated(new Date());
        brandEntity.setDateUpdated(new Date());
        brandRepository.save(brandEntity);
        log.info("Successfully saved Brand : " + brandEntity.getId());

        Brand mappedBrand = modelMapper.map(brandEntity, Brand.class);
        mappedBrand.setImage(ShoeMartConstant.IMAGE_FOLDER_BRAND + "/" + brandEntity.getImage());
        response.setMessage("Successfully saved Brand");
        response.setStatus(HttpStatus.CREATED.value());
        response.setObject(mappedBrand);
        return response;
    }

    @Override
    public List<Brand> findAllBrand() {
        Sort sort = Sort.by(Sort.Direction.ASC, "indexSeq");
        return brandRepository.findAllByIsActiveIsTrue(sort).stream()
                .map(brand -> {
                    brand.setImage(ShoeMartConstant.IMAGE_FOLDER_BRAND + "/" + brand.getImage());
                    return modelMapper.map(brand, Brand.class);
                }).collect(Collectors.toList());
    }

    @Override
    public ApiResponse<Brand> getBrand(Long id) {
        ApiResponse<Brand> response = new ApiResponse<>();
        if (id == null) {
            throw new LsmException("Brand id can't be empty");
        }
        Optional<com.ushan.lady_shoe_mart.product.entity.Brand> brandEntity = brandRepository.findByIdAndIsActiveIsTrue(id);
        if (brandEntity.isEmpty()) {
            throw new LsmException("Brand isn't exist");
        }
        Brand mappedBrand = modelMapper.map(brandEntity.get(), Brand.class);
        mappedBrand.setImage(ShoeMartConstant.IMAGE_FOLDER_BRAND + "/" + brandEntity.get().getImage());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Brand retrieved successfully");
        response.setObject(mappedBrand);
        return response;
    }

    @Transactional
    @Override
    public ApiResponse<Brand> update(Long id, BrandRequest brand) {
        ApiResponse<Brand> response = new ApiResponse<>();

        if (id == null) {
            throw new LsmException("Brand id can't be empty");
        }
        if (brand.getName() == null || brand.getName().isEmpty()) {
            throw new LsmException("Brand name can't be empty");
        }
        if (brand.getActive() == null) {
            throw new LsmException("Active status can't be empty");
        }

        com.ushan.lady_shoe_mart.product.entity.Brand brandEntity = brandRepository.findByIdAndIsActiveIsTrue(id)
                        .orElseThrow(() -> new LsmException("Brand does not exist"));
        brandEntity.setName(brand.getName());
        brandEntity.setActive(brand.getActive());
        brandEntity.setIndexSeq(
                brand.getIndexSeq() == null || brand.getIndexSeq() < 1
                        ? brandEntity.getIndexSeq()
                        : brand.getIndexSeq());
        brandEntity.setDateUpdated(new Date());

        if (brand.getImage() != null) {
            brandEntity.setImage(imageService.uploadFile(brand.getImage(), ShoeMartConstant.IMAGE_FOLDER_BRAND, ShoeMartConstant.IMAGE_PREFIX_BRAND));
        }
        brandRepository.save(brandEntity);
        Brand mappedBrand = modelMapper.map(brandEntity, Brand.class);
        mappedBrand.setImage(ShoeMartConstant.IMAGE_FOLDER_BRAND + "/" + brandEntity.getImage());

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Brand updated successfully");
        response.setObject(mappedBrand);
        return response;
    }

    private void brandCodeIsExist(String brandCode) {
        if (brandRepository.findByBrandCode(brandCode).isPresent()) {
            throw new LsmException("Brand Code is exist, Change the name and retry");
        }
    }
}
