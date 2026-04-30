package com.ushan.lady_shoe_mart.auth.repository;

import com.ushan.lady_shoe_mart.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    List<Role> findAllByActiveIsTrueAndIsActiveIsTrue();

    Role findByIdAndActiveIsTrueAndIsActiveIsTrueAndRolePermissionList_ActiveIsTrueAndRolePermissionList_IsActiveIsTrue(Long id);
}
