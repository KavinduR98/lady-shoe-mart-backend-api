package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.Category;
import com.ushan.lady_shoe_mart.admin.domain.SubCategory;
import com.ushan.lady_shoe_mart.admin.repository.CategoryRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.common.util.ShoeMartConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<Category> save(Category category) {
        ApiResponse<Category> response = new ApiResponse<>();
        if (category.getName() == null) {
            throw new LsmException("Category name can't be empty");
        }
        if (category.getCategoryCode() == null) {
            throw new LsmException("Category code can't be empty");
        }
        com.ushan.lady_shoe_mart.admin.entity.Category categoryFindByName = categoryRepository.findByNameIgnoreCase(category.getName());
        if (categoryFindByName != null) throw new LsmException("Category name exist, change the name and retry!");
        Boolean categoryCodeExist = categoryRepository.existsByCategoryCodeIgnoreCase(category.getCategoryCode());
        if (categoryCodeExist) throw new LsmException("Category code exist!, change the category code and retry!");
        if (category.getActive() == null) throw new LsmException("Active can't be empty");
        com.ushan.lady_shoe_mart.admin.entity.Category categoryEntity = modelMapper.map(category, com.ushan.lady_shoe_mart.admin.entity.Category.class);
        categoryEntity.setCategoryCode(category.getCategoryCode().toUpperCase());
        categoryEntity.setDateCreated(category.getDateCreated());
        categoryEntity.setDateUpdated(category.getDateUpdated());
        categoryEntity.setIsActive(category.getIsActive());
        categoryRepository.save(categoryEntity);

        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Category is saved");
        response.setObject(categoryMapper(categoryEntity));
        return response;
    }

    private Category categoryMapper(com.ushan.lady_shoe_mart.admin.entity.Category category) {
        Category categoryDomain = modelMapper.map(category, Category.class);
        if (category.getImageLink() != null) {
            categoryDomain.setImageLink(ShoeMartConstant.IMAGE_FOLDER_BRAND + "/" + category.getImageLink());
        }
        if (category.getSubCategoryList() != null) {
            categoryDomain.setSubCategoryList(
                    category.getSubCategoryList()
                            .stream()
                            .filter(sc -> Boolean.TRUE.equals(sc.getIsActive()))
                            .map(subCategory -> modelMapper.map(subCategory,SubCategory.class))
                            .collect(Collectors.toList())
            );
        }
        return categoryDomain;
    }
}
