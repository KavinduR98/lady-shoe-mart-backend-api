package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.SubCategory;
import com.ushan.lady_shoe_mart.admin.entity.Category;
import com.ushan.lady_shoe_mart.admin.repository.CategoryRepository;
import com.ushan.lady_shoe_mart.admin.repository.SubcategoryRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class SubcategoryService implements ISubcategoryService{

    private final SubcategoryRepository subcategoryRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse<SubCategory> save(SubCategory subCategory) {
        ApiResponse<SubCategory> response = new ApiResponse<>();
        if (subCategory.getName() == null) throw new LsmException("Sub category name can't be empty");
        if (subCategory.getCategoryId() == null) throw new LsmException("Category id can't be empty");
        if (subCategory.getSubCategoryCode() == null || subCategory.getSubCategoryCode().isEmpty()) throw new LsmException("Sub category code can't be empty!");
        if (subCategory.getActive() == null) throw new LsmException("Active can't be empty");

        Boolean subCategoryCodeExist = subcategoryRepository.existsBySubCategoryCodeIgnoreCase(subCategory.getCategoryCode());
        if (subCategoryCodeExist) {
            throw new LsmException("Sub Category code exist, change the sub category code and retry!");
        }
        com.ushan.lady_shoe_mart.admin.entity.SubCategory subCategoryEntity = modelMapper.map(subCategory, com.ushan.lady_shoe_mart.admin.entity.SubCategory.class);
        Optional<Category> categoryOptional = categoryRepository.findById(subCategory.getCategoryId());
        if (categoryOptional.isEmpty()) {
            throw new LsmException("Category Not Found!");
        }
        subCategoryEntity.setCategory(categoryOptional.get());
        subCategoryEntity.setSubCategoryCode(subCategory.getSubCategoryCode().toUpperCase());
        subCategoryEntity.setDateCreated(new Date());
        subCategoryEntity.setDateUpdated(new Date());
        subCategoryEntity.setIsActive(Boolean.TRUE);
        subcategoryRepository.save(subCategoryEntity);
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Successfully Saved SubCategory");
        response.setObject(subCategoryMapper(subCategoryEntity));
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubCategory> findAllSubCategory() {
        return subcategoryRepository.findAllByIsActiveIsTrue().stream().map(this::subCategoryMapper).collect(Collectors.toList());
    }

    @Override
    public ApiResponse<Boolean> active(Long id, Boolean active) {
        ApiResponse<Boolean> response = new ApiResponse<>();

        if (active == null) throw new LsmException("Active status can't be empty");
        com.ushan.lady_shoe_mart.admin.entity.SubCategory subCategory = findSubCategoryById(id);
        if (subCategory.getActive() == active) throw new LsmException("Already updated active status");
        subCategory.setActive(active);
        subCategory.setDateUpdated(new Date());
        subcategoryRepository.save(subCategory);
        response.setMessage("Successfully updated active status");
        response.setObject(Boolean.TRUE);
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubCategory> findByCategoryId(Long id) {
        return subcategoryRepository.findAllByCategory_IdAndActiveIsTrueAndIsActiveIsTrue(id).stream().map(this::subCategoryMapper).collect(Collectors.toList());
    }

    @Override
    public ApiResponse<SubCategory> update(SubCategory subCategory) {
        ApiResponse<SubCategory> response = new ApiResponse<>();
        if (subCategory.getId() == null) throw new LsmException("Sub category id empty!");
        if (subCategory.getName() == null) throw new LsmException("Sub category name can't be empty");
        if (subCategory.getCategoryId() == null) throw new LsmException("Category id can't be empty");
        if (subCategory.getSubCategoryCode() == null) throw new LsmException("Sub Category code can't be empty");

        Optional<com.ushan.lady_shoe_mart.admin.entity.SubCategory> subCategoryOptional = subcategoryRepository.findById(subCategory.getId());
        if (subCategoryOptional.isEmpty()) throw new LsmException("Sub category not found!");
        com.ushan.lady_shoe_mart.admin.entity.SubCategory subCategoryEntity = subCategoryOptional.get();
        if (!subCategoryEntity.getSubCategoryCode().equalsIgnoreCase(subCategory.getCategoryCode())) {
            Boolean subCategoryCodeExist = subcategoryRepository.existsBySubCategoryCodeIgnoreCase(subCategory.getSubCategoryCode());
            if (subCategoryCodeExist) {
                throw new LsmException("Sub Category code exist, change the sub category code and retry!");
            }
        }
        if (!subCategoryEntity.getCategory().getId().equals(subCategory.getCategoryId())) {
            Optional<Category> categoryOptional = categoryRepository.findById(subCategory.getCategoryId());
            if (categoryOptional.isEmpty()) {
                throw new LsmException("Category Not Found!");
            }
            subCategoryEntity.setCategory(categoryOptional.get());
        }
        modelMapper.map(subCategory, subCategoryEntity);
        subCategoryEntity.setDateUpdated(new Date());
        subcategoryRepository.save(subCategoryEntity);

        response.setStatus(HttpStatus.OK.value());
        response.setObject(subCategoryMapper(subCategoryEntity));
        response.setMessage("Successfully Updated SubCategory!");
        return response;
    }

    public com.ushan.lady_shoe_mart.admin.entity.SubCategory findSubCategoryById(Long id) {
        if (id == null || id == 0) throw new LsmException("Id can't be empty");
        Optional<com.ushan.lady_shoe_mart.admin.entity.SubCategory> optionalSubCategory = subcategoryRepository.findByIdAndIsActiveIsTrue(id);
        if (optionalSubCategory.isEmpty()) throw new LsmException("Sub Category not found");
        return optionalSubCategory.get();
    }

    private SubCategory subCategoryMapper(com.ushan.lady_shoe_mart.admin.entity.SubCategory subCategory) {
        SubCategory subCategoryDomain = modelMapper.map(subCategory, SubCategory.class);
        subCategoryDomain.setCategoryId(subCategory.getCategory().getId());
        subCategoryDomain.setCategoryCode(subCategory.getCategory().getCategoryCode());
        subCategoryDomain.setCategoryName(subCategory.getCategory().getName());
        return subCategoryDomain;
    }

}
