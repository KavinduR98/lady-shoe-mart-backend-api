package com.ushan.lady_shoe_mart.admin.repository;

import com.ushan.lady_shoe_mart.admin.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubCategory, Long> {

    Boolean existsBySubCategoryCodeIgnoreCase(String var1);

    List<SubCategory> findAllByIsActiveIsTrue();

    Optional<SubCategory> findByIdAndIsActiveIsTrue(Long id);

    List<SubCategory> findAllByCategory_IdAndActiveIsTrueAndIsActiveIsTrue(Long categoryId);
}
