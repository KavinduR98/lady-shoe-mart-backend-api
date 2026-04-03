package com.ushan.lady_shoe_mart.admin.repository;

import com.ushan.lady_shoe_mart.admin.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Boolean existsProductByErpCodeIgnoreCase(String var1);

    @Query(value = """
            SELECT IFNULL(
                MAX(CAST(
                    SUBSTRING(p.product_number, :prefixLength + 1, LENGTH(p.product_number))
                    AS SIGNED
                )), 0)
            FROM product p
            WHERE p.product_number LIKE CONCAT(:prefix, '%')
              AND p.is_active = 1
            """, nativeQuery = true)
    int findMaxProductNumberSuffix(
            @Param("prefix") String prefix,
            @Param("prefixLength") int prefixLength
    );
}
