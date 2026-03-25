package com.ushan.lady_shoe_mart.product.repository;

import com.ushan.lady_shoe_mart.product.entity.Brand;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand>, PagingAndSortingRepository<Brand, Long> {
    Optional<Brand> findByBrandCode(String brandCode);

    List<Brand> findAllByIsActiveIsTrue(Sort var1);

    Optional<Brand> findByIdAndIsActiveIsTrue(Long id);
}
