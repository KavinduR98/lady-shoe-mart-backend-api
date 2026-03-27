package com.ushan.lady_shoe_mart.admin.repository;

import com.ushan.lady_shoe_mart.admin.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Category findByNameIgnoreCase(String var1);

    Boolean existsByCategoryCodeIgnoreCase(String var1);
}
