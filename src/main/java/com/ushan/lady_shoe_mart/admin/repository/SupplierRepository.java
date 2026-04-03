package com.ushan.lady_shoe_mart.admin.repository;

import com.ushan.lady_shoe_mart.admin.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {

    Supplier findBySupplierIdIgnoreCase(String var1);

    List<Supplier> findAllByIsActiveIsTrue();

    Supplier findByIdAndIsActiveTrue(Long id);
}
