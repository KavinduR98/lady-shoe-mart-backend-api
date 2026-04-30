package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.Role;
import com.ushan.lady_shoe_mart.auth.entity.Permission;
import com.ushan.lady_shoe_mart.auth.entity.RolePermission;
import com.ushan.lady_shoe_mart.auth.repository.PermissionRepository;
import com.ushan.lady_shoe_mart.auth.repository.RolePermissionRepository;
import com.ushan.lady_shoe_mart.auth.repository.RoleRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ApiResponse<Role> save(Role role) {
        ApiResponse<Role> response = new ApiResponse<>();

        if (role.getName() == null) {
            throw new LsmException("Role name is can't be empty!");
        }

        com.ushan.lady_shoe_mart.auth.entity.Role roleNameExist = roleRepository.findByName(role.getName());
        if (roleNameExist != null) {
            throw new LsmException("Role name exist, change the role name and retry!");
        }
        com.ushan.lady_shoe_mart.auth.entity.Role roleDao = new com.ushan.lady_shoe_mart.auth.entity.Role();
        roleDao.setName(role.getName());
        roleDao.setActive(Boolean.TRUE);
        roleDao.setDateCreated(new Date());
        roleDao.setDateUpdated(new Date());
        roleDao.setIsActive(true);
        roleDao.setIsSuper(role.getIsSuper() );
        List<RolePermission> rolePermissionList = new ArrayList<>();
        if (role.getRolePermissionList() != null) {
            for (com.ushan.lady_shoe_mart.auth.domain.RolePermission rolePermission : role.getRolePermissionList()) {
                if (rolePermission.getPermissionId() == null) {
                    throw new LsmException("Permission Id can't be empty!");
                }
                Boolean rolePermissionExist = rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermission.getPermissionId(), roleDao.getId());
                if (rolePermissionExist) {
                    throw new LsmException("Role permission exist!");
                }
                Optional<Permission> permissionOptional = permissionRepository.findById(rolePermission.getPermissionId());
                if (permissionOptional.isEmpty()) {
                    throw new LsmException("Permission not found!");
                }
                RolePermission rolePermissionDao = new RolePermission();
                rolePermissionDao.setRole(roleDao);
                rolePermissionDao.setPermission(permissionOptional.get());
                rolePermissionDao.setActive(Boolean.TRUE);
                rolePermissionDao.setDateCreated(new Date());
                rolePermissionDao.setDateUpdated(new Date());
                rolePermissionDao.setIsActive(Boolean.TRUE);
                rolePermissionList.add(rolePermissionDao);
            }
        }
        roleRepository.save(roleDao);
        if (!rolePermissionList.isEmpty()) {
            rolePermissionRepository.saveAll(rolePermissionList);
        }
        roleDao.setRolePermissionList(rolePermissionList);
        response.setMessage("success");
        response.setObject(roleMapper(roleDao));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRole() {
        List<com.ushan.lady_shoe_mart.auth.entity.Role> roleDaoList = roleRepository.findAllByActiveIsTrueAndIsActiveIsTrue();
        return roleDaoList.stream().map(this::roleMapper).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Role findAllRoleById(Long roleId) {
        com.ushan.lady_shoe_mart.auth.entity.Role role = roleRepository.findByIdAndActiveIsTrueAndIsActiveIsTrueAndRolePermissionList_ActiveIsTrueAndRolePermissionList_IsActiveIsTrue(roleId);
        return roleMapper(role);
    }

    @Override
    @Transactional
    public ApiResponse<Role> update(Role role) {
        ApiResponse<Role> response = new ApiResponse<>();
        if (role.getId() == null) {
            throw new LsmException("Role id is empty!");
        }
        Optional<com.ushan.lady_shoe_mart.auth.entity.Role> roleOptional = roleRepository.findById(role.getId());
        if (roleOptional.isEmpty()) {
            throw new LsmException("Role not found!");
        }
        com.ushan.lady_shoe_mart.auth.entity.Role roleDao = roleOptional.get();
        if (!roleDao.getName().equals(role.getName())) {
            com.ushan.lady_shoe_mart.auth.entity.Role roleNameExist = roleRepository.findByName(role.getName());
            if (roleNameExist != null) {
                throw new LsmException("Role name exist, change the role name and retry!");
            }
            roleDao.setName(role.getName());
        }
        if (role.getIsSuper() != null && !role.getIsSuper().equals(roleDao.getIsSuper())) {
            roleDao.setIsSuper(role.getIsSuper());
        }
        if (role.getActive() != null && !role.getActive().equals(roleDao.getActive())) {
            roleDao.setActive(role.getActive());
        }
        if (role.getIsActive() != null && !role.getIsActive().equals(roleDao.getIsActive())) {
            roleDao.setIsActive(role.getIsActive());
        }
        roleDao.setDateUpdated(new Date());
        List<RolePermission> rolePermissionList = new ArrayList<>();
        if (role.getRolePermissionList() != null) {
            for (com.ushan.lady_shoe_mart.auth.domain.RolePermission rolePermission : role.getRolePermissionList()) {
                if (rolePermission.getId() != null) {
                    if (rolePermission.getPermissionId() == null) {
                        throw new LsmException("Permission Id can't be empty!");
                    }
                    Optional<RolePermission> rolePermissionOptional = rolePermissionRepository.findById(rolePermission.getId());
                    if (rolePermissionOptional.isEmpty()) {
                        throw new LsmException("Role permission not found!");
                    }
                    RolePermission rolePermissionDao = rolePermissionOptional.get();
                    rolePermissionDao.setRole(roleDao);
                    if (!rolePermissionDao.getPermission().getId().equals(rolePermission.getPermissionId())) {
                        Boolean rolePermissionExist = rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermission.getPermissionId(), roleDao.getId());
                        if (rolePermissionExist) {
                            throw new LsmException("Role permission exist!");
                        }
                        Optional<Permission> permissionOptional = permissionRepository.findById(rolePermission.getPermissionId());
                        if (permissionOptional.isEmpty()) {
                            throw new LsmException("Permission not found!");
                        }
                    }
                    rolePermissionDao.setDateUpdated(new Date());
                    rolePermissionDao.setActive(rolePermission.getActive());
                    rolePermissionDao.setIsActive(rolePermission.getIsActive());
                    rolePermissionList.add(rolePermissionDao);
                } else {
                    if (rolePermission.getPermissionId() == null) {
                        throw new LsmException("Permission Id can't be empty!");
                    }
                    Boolean rolePermissionExist = rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermission.getPermissionId(), roleDao.getId());
                    if (rolePermissionExist) {
                        throw new LsmException("Role permission exist!");
                    }
                    Optional<Permission> permissionOptional = permissionRepository.findById(rolePermission.getPermissionId());
                    if (permissionOptional.isEmpty()) {
                        throw new LsmException("Permission not found!");
                    }
                    RolePermission rolePermissionDao = new RolePermission();
                    rolePermissionDao.setRole(roleDao);
                    rolePermissionDao.setPermission(permissionOptional.get());
                    rolePermissionDao.setActive(true);
                    rolePermissionDao.setDateCreated(new Date());
                    rolePermissionDao.setDateUpdated(new Date());
                    rolePermissionDao.setIsActive(true);
                    rolePermissionList.add(rolePermissionDao);
                }
            }
        }
        roleRepository.save(roleDao);
        if (!rolePermissionList.isEmpty()) {
            rolePermissionRepository.saveAll(rolePermissionList);
        }
        response.setObject(roleMapper(roleDao));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    private Role roleMapper(com.ushan.lady_shoe_mart.auth.entity.Role roleEntity) {
        Role role = modelMapper.map(roleEntity, Role.class);
        List<com.ushan.lady_shoe_mart.auth.domain.RolePermission> rolePermissionList = new ArrayList<>();
        if (roleEntity.getRolePermissionList() != null && !roleEntity.getRolePermissionList().isEmpty()) {
             for (RolePermission rolePermission : roleEntity.getRolePermissionList()) {
                if (rolePermission.getIsActive() != null && rolePermission.getIsActive()) {
                    com.ushan.lady_shoe_mart.auth.domain.RolePermission permission = modelMapper.map(rolePermission, com.ushan.lady_shoe_mart.auth.domain.RolePermission.class);
                    rolePermissionList.add(permission);
                }
             }
            role.setRolePermissionList(rolePermissionList);
        }
        return role;
    }
}
