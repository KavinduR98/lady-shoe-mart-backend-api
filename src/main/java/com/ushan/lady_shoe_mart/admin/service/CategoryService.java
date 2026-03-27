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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final IImageService imageService;

    @Value("IMAGE_BASE_URL")
    private String baseurl;

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

    @Transactional(readOnly = true)
    @Override
    public List<Category> findAllCategory() {
        return categoryRepository.findAllByIsActiveIsTrue().stream().map(this::categoryMapper).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ApiResponse<String> categoryImageUpload(MultipartFile file, Long categoryId) {
        ApiResponse<String> response = new ApiResponse<>();
        com.ushan.lady_shoe_mart.admin.entity.Category categoryEntity = findCategoryById(categoryId);
        String imageName = imageService.uploadFile(file, ShoeMartConstant.IMAGE_FOLDER_CATEGORY, ShoeMartConstant.IMAGE_PREFIX_CATEGORY);
        categoryEntity.setImageLink(imageName);
        String imageLink = baseurl + ShoeMartConstant.IMAGE_FOLDER_CATEGORY + "/" + imageName;
        categoryRepository.save(categoryEntity);
        log.info("Successfully image upload: {}", imageLink);
        response.setObject(imageLink);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Successfully image upload");
        return response;
    }

    @Override
    public ApiResponse<Boolean> active(Long id, Boolean active) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        if (active == null) throw new LsmException("Active status can't be empty");
        com.ushan.lady_shoe_mart.admin.entity.Category categoryEntity = findCategoryById(id);
        if (categoryEntity.getActive() == active) throw new LsmException("Already updated active status");
        categoryEntity.setActive(active);
        categoryEntity.setDateUpdated(new Date());
        categoryRepository.save(categoryEntity);
        response.setMessage("Successfully updated active status");
        response.setObject(Boolean.TRUE);
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    private com.ushan.lady_shoe_mart.admin.entity.Category findCategoryById(Long categoryId) {
        if (categoryId == null || categoryId == 0) throw new LsmException("Id can't be empty");
        return categoryRepository.findById(categoryId).orElseThrow(()-> new LsmException("Category not found"));
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
