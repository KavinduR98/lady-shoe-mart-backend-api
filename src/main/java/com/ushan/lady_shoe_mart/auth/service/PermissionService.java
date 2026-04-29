package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.Permission;
import com.ushan.lady_shoe_mart.auth.domain.response.PermissionResponse;
import com.ushan.lady_shoe_mart.auth.repository.PermissionRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionCategory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PermissionService implements IPermissionService{

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public List<Permission> save(List<Permission> permissionList) {
        List<com.ushan.lady_shoe_mart.auth.entity.Permission> permissionListDao = new ArrayList<>();
        for (Permission permission : permissionList) {
            com.ushan.lady_shoe_mart.auth.entity.Permission permissionEntity;
            if (permission.getName() == null) {
                throw new LsmException("Permission name can't be empty!");
            }
            if (permission.getValue() == null) {
                throw new LsmException("Permission value can't be empty!");
            }
            if (permission.getPermissionType() == null) {
                throw new LsmException("Permission type can't be empty!");
            }
            if (permission.getId() == null) {
                if (permissionRepository.existsByNameAndActiveIsTrueAndIsActiveTrue(permission.getName())) {
                    throw new LsmException("Permission name exist!");
                }
                if (permissionRepository.existsByValueAndActiveIsTrueAndIsActiveTrue(permission.getValue())) {
                    throw new LsmException("Permission value exist!");
                }
                permissionEntity = modelMapper.map(permission, com.ushan.lady_shoe_mart.auth.entity.Permission.class);
                permissionEntity.setActive(permission.getActive() != null ? permission.getActive() : false);
                permissionEntity.setIsActive(true);
                permissionEntity.setDateCreated(new Date());
                permissionEntity.setDateUpdated(new Date());
                permissionListDao.add(permissionEntity);
            } else {
                Optional<com.ushan.lady_shoe_mart.auth.entity.Permission> permissionOptional = permissionRepository.findById(permission.getId());
                if (permissionOptional.isEmpty()) {
                    throw new LsmException("Permission not found!");
                }
                com.ushan.lady_shoe_mart.auth.entity.Permission permissionUpdateEntity = permissionOptional.get();
                if (!permission.getName().equals(permissionUpdateEntity.getName())) {
                    if (permissionRepository.existsByNameAndActiveIsTrueAndIsActiveTrue(permission.getName())) {
                        throw new LsmException("Permission name exist!");
                    }
                }
                if (!permission.getValue().equals(permissionUpdateEntity.getValue())) {
                    throw new LsmException("Permission value exist!");
                }
                permissionUpdateEntity = modelMapper.map(permission, com.ushan.lady_shoe_mart.auth.entity.Permission.class);
                permissionUpdateEntity.setDateUpdated(new Date());
                permissionListDao.add(permissionUpdateEntity);
            }
        }
        permissionRepository.saveAll(permissionListDao);
        return permissionListDao.stream().map(m -> modelMapper.map(m, Permission.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Permission> findAllPermission() {
        List<com.ushan.lady_shoe_mart.auth.entity.Permission> permissionList = permissionRepository.findAllIsActiveIsTrueAndActiveIsTrue();
        return permissionList.stream().map(m -> modelMapper.map(m, Permission.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<PermissionResponse> findAllPermissionGroup() {
        List<com.ushan.lady_shoe_mart.auth.entity.Permission> permissionList = permissionRepository.findAllIsActiveIsTrueAndActiveIsTrue();
        Map<PermissionCategory, List<Permission>> permissionMap = new HashMap<>();
        for (com.ushan.lady_shoe_mart.auth.entity.Permission permission : permissionList) {
            PermissionCategory key = permission.getPermissionCategory();
            if (!permissionMap.containsKey(key)) {
               List<Permission> list = new ArrayList<>();
               list.add(modelMapper.map(permission, Permission.class));
               permissionMap.put(key, list);
            } else {
                permissionMap.get(key).add(modelMapper.map(permission, Permission.class));
            }
        }
        List<PermissionResponse> permissionResponses = new ArrayList<>();
        permissionMap.forEach(((category, permissions) -> {
            PermissionResponse response = new PermissionResponse();
            response.setCategory(category);
            response.setPermissions(permissions);
            permissionResponses.add(response);
        }));
        return permissionResponses;
    }
}
