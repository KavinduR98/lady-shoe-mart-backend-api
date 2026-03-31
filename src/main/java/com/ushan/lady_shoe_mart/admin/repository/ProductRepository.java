package com.ushan.lady_shoe_mart.admin.repository;

import com.ushan.lady_shoe_mart.admin.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Boolean existsProductByErpCodeIgnoreCase(String var1);
}
