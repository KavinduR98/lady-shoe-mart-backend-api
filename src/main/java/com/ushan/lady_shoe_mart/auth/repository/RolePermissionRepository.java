package com.ushan.lady_shoe_mart.auth.repository;

import com.ushan.lady_shoe_mart.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {

    Boolean existsByPermission_IdAndRole_IdAndIsActiveIsTrue(Long var1, Long var2);
}
