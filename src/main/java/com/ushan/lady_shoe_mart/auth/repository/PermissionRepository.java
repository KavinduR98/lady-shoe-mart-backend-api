package com.ushan.lady_shoe_mart.auth.repository;

import com.ushan.lady_shoe_mart.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    Boolean existsByNameAndActiveIsTrueAndIsActiveTrue(String permissionName);

    Boolean existsByValueAndActiveIsTrueAndIsActiveTrue(String permissionValue);

    List<Permission> findAllByIsActiveIsTrueAndActiveIsTrue();
}
