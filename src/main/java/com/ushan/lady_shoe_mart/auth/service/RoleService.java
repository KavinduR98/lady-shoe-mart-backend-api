package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.RoleDto;
import com.ushan.lady_shoe_mart.auth.domain.RolePermissionDto;
import com.ushan.lady_shoe_mart.auth.entity.Permission;
import com.ushan.lady_shoe_mart.auth.entity.Role;
import com.ushan.lady_shoe_mart.auth.entity.RolePermission;
import com.ushan.lady_shoe_mart.auth.repository.PermissionRepository;
import com.ushan.lady_shoe_mart.auth.repository.RolePermissionRepository;
import com.ushan.lady_shoe_mart.auth.repository.RoleRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ApiResponse<RoleDto> save(RoleDto role) {
        ApiResponse<RoleDto> response = new ApiResponse<>();

        if (role.getName() == null) {
            throw new LsmException("RoleDto name is can't be empty!");
        }

        com.ushan.lady_shoe_mart.auth.entity.Role roleNameExist = roleRepository.findByName(role.getName());
        if (roleNameExist != null) {
            throw new LsmException("RoleDto name exist, change the role name and retry!");
        }
        com.ushan.lady_shoe_mart.auth.entity.Role roleDao = new com.ushan.lady_shoe_mart.auth.entity.Role();
        roleDao.setName(role.getName());
        roleDao.setActive(Boolean.TRUE);
        roleDao.setDateCreated(new Date());
        roleDao.setDateUpdated(new Date());
        roleDao.setIsActive(true);
        roleDao.setIsSuper(role.getIsSuper() != null ? roleDao.getIsSuper() : Boolean.FALSE);
        roleRepository.save(roleDao);
        log.info("Saved role: {}", roleDao.getName());

        List<RolePermission> rolePermissions = createRolePermissions(role.getRolePermissionList(), roleDao);

        if (!rolePermissions.isEmpty()) {
            rolePermissionRepository.saveAll(rolePermissions);
        }
        roleDao.setRolePermissionList(rolePermissions);
        response.setMessage("success");
        response.setObject(roleMapper(roleDao));
        response.setStatus(HttpStatus.CREATED.value());
        return response;
    }

    private List<RolePermission> createRolePermissions(List<RolePermissionDto> rolePermissionDtoList, Role roleEntity) {

        List<RolePermission> rolePermissionList = new ArrayList<>();
        if (rolePermissionDtoList == null) return rolePermissionList;

        for (RolePermissionDto rolePermissionDto : rolePermissionDtoList) {
            if (rolePermissionDto.getPermissionId() == null) {
                throw new LsmException("Permission Id can't be empty");
            }
            if (rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermissionDto.getPermissionId(), roleEntity.getId())) {
                throw new LsmException("Permission already assigned to this role");
            }

            Permission permission = permissionRepository.findById(rolePermissionDto.getPermissionId())
                    .orElseThrow(() -> new LsmException("Permission not found: " + rolePermissionDto.getPermissionId()));

            RolePermission rp = new RolePermission();
            rp.setRole(roleEntity);
            rp.setPermission(permission);
            rp.setActive(Boolean.TRUE);
            rp.setIsActive(Boolean.TRUE);
            rp.setDateCreated(new Date());
            rp.setDateUpdated(new Date());
            rolePermissionList.add(rp);
        }
        return rolePermissionList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> findAllRole() {
        List<com.ushan.lady_shoe_mart.auth.entity.Role> roleDaoList = roleRepository.findAllByActiveIsTrueAndIsActiveIsTrue();
        return roleDaoList.stream().map(this::roleMapper).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto findAllRoleById(Long roleId) {
        com.ushan.lady_shoe_mart.auth.entity.Role role = roleRepository.findByIdAndActiveIsTrueAndIsActiveIsTrueAndRolePermissionList_ActiveIsTrueAndRolePermissionList_IsActiveIsTrue(roleId);
        return roleMapper(role);
    }

    @Override
    @Transactional
    public ApiResponse<RoleDto> update(RoleDto role) {
        ApiResponse<RoleDto> response = new ApiResponse<>();
        if (role.getId() == null) throw new LsmException("Role id is empty!");

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
            for (RolePermissionDto rolePermission : role.getRolePermissionList()) {
                if (rolePermission.getId() != null) {
                    if (rolePermission.getPermissionId() == null) {
                        throw new LsmException("PermissionDto Id can't be empty!");
                    }
                    Optional<RolePermission> rolePermissionOptional = rolePermissionRepository.findById(rolePermission.getId());
                    if (rolePermissionOptional.isEmpty()) {
                        throw new LsmException("RoleDto permission not found!");
                    }
                    RolePermission rolePermissionDao = rolePermissionOptional.get();
                    rolePermissionDao.setRole(roleDao);
                    if (!rolePermissionDao.getPermission().getId().equals(rolePermission.getPermissionId())) {
                        Boolean rolePermissionExist = rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermission.getPermissionId(), roleDao.getId());
                        if (rolePermissionExist) {
                            throw new LsmException("RoleDto permission exist!");
                        }
                        Optional<Permission> permissionOptional = permissionRepository.findById(rolePermission.getPermissionId());
                        if (permissionOptional.isEmpty()) {
                            throw new LsmException("PermissionDto not found!");
                        }
                    }
                    rolePermissionDao.setDateUpdated(new Date());
                    rolePermissionDao.setActive(rolePermission.getActive());
                    rolePermissionDao.setIsActive(rolePermission.getIsActive());
                    rolePermissionList.add(rolePermissionDao);
                } else {
                    if (rolePermission.getPermissionId() == null) {
                        throw new LsmException("PermissionDto Id can't be empty!");
                    }
                    Boolean rolePermissionExist = rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermission.getPermissionId(), roleDao.getId());
                    if (rolePermissionExist) {
                        throw new LsmException("RoleDto permission exist!");
                    }
                    Optional<Permission> permissionOptional = permissionRepository.findById(rolePermission.getPermissionId());
                    if (permissionOptional.isEmpty()) {
                        throw new LsmException("PermissionDto not found!");
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
        if (role.getRolePermissionList() != null) {
            List<RolePermission> updatedPermissions = updateRolePermissions(role.getRolePermissionList(), roleDao);
            if (!updatedPermissions.isEmpty()) {
                rolePermissionRepository.saveAll(updatedPermissions);
            }
        }
        roleRepository.save(roleDao);
        response.setObject(roleMapper(roleDao));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    private List<RolePermission> updateRolePermissions(List<RolePermissionDto> rolePermissionDtoList, Role roleEntity) {

        List<RolePermission> rolePermissionList = new ArrayList<>();

        for (RolePermissionDto rolePermissionDto : rolePermissionDtoList) {
            if (rolePermissionDto.getPermissionId() == null) {
                throw new LsmException("Permission Id can't be empty");
            }
            if (rolePermissionDto.getId() != null) {
                // ── Update existing RolePermission entry ──
                RolePermission existing = rolePermissionRepository.findById(rolePermissionDto.getId())
                        .orElseThrow(() -> new LsmException("Role permission not found: " + rolePermissionDto.getId()));

                // If permission is changing, check for duplicate and reassign
                if (!existing.getPermission().getId().equals(rolePermissionDto.getPermissionId())) {
                    if (rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermissionDto.getPermissionId(), roleEntity.getId())) {
                        throw new LsmException("Permission already assigned to this role");
                    }
                    Permission newPermission = permissionRepository.findById(rolePermissionDto.getPermissionId())
                            .orElseThrow(() -> new LsmException("Permission not found: " + rolePermissionDto.getPermissionId()));
                    // set the new permission
                    existing.setPermission(newPermission);
                }

                if (rolePermissionDto.getActive() != null) existing.setActive(rolePermissionDto.getActive());
                if (rolePermissionDto.getIsActive() != null) existing.setIsActive(rolePermissionDto.getIsActive());
                existing.setDateUpdated(new Date());
                rolePermissionList.add(existing);
            } else {
                // ── Add new RolePermission entry ───
                if (rolePermissionRepository.existsByPermission_IdAndRole_IdAndIsActiveIsTrue(rolePermissionDto.getPermissionId(), roleEntity.getId())) {
                    throw new LsmException("Permission already assigned to this role");
                }
                Permission permission = permissionRepository.findById(rolePermissionDto.getPermissionId())
                        .orElseThrow(() -> new LsmException("Permission not found: " + rolePermissionDto.getPermissionId()));

                RolePermission newRp = new RolePermission();
                newRp.setRole(roleEntity);
                newRp.setPermission(permission);
                newRp.setActive(Boolean.TRUE);
                newRp.setIsActive(Boolean.TRUE);
                newRp.setDateCreated(new Date());
                newRp.setDateUpdated(new Date());
                rolePermissionList.add(newRp);
            }
        }
        return rolePermissionList;
    }

    private RoleDto roleMapper(com.ushan.lady_shoe_mart.auth.entity.Role roleEntity) {
        RoleDto role = modelMapper.map(roleEntity, RoleDto.class);
        List<RolePermissionDto> rolePermissionList = new ArrayList<>();
        if (roleEntity.getRolePermissionList() != null && !roleEntity.getRolePermissionList().isEmpty()) {
             for (RolePermission rolePermission : roleEntity.getRolePermissionList()) {
                if (rolePermission.getIsActive() != null && rolePermission.getIsActive()) {
                    RolePermissionDto permission = modelMapper.map(rolePermission, RolePermissionDto.class);
                    rolePermissionList.add(permission);
                }
             }
            role.setRolePermissionList(rolePermissionList);
        }
        return role;
    }
}
