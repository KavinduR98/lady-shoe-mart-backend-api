package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.ActionPermission;
import com.ushan.lady_shoe_mart.auth.domain.ViewPermission;
import com.ushan.lady_shoe_mart.auth.domain.request.AuthRequest;
import com.ushan.lady_shoe_mart.auth.domain.response.AuthResponse;
import com.ushan.lady_shoe_mart.auth.domain.response.RoleResponse;
import com.ushan.lady_shoe_mart.auth.entity.Role;
import com.ushan.lady_shoe_mart.auth.entity.RolePermission;
import com.ushan.lady_shoe_mart.auth.entity.User;
import com.ushan.lady_shoe_mart.auth.entity.UserLogin;
import com.ushan.lady_shoe_mart.auth.repository.UserLoginRepository;
import com.ushan.lady_shoe_mart.auth.repository.UserRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.common.util.JwtUtil;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public ApiResponse<AuthResponse> createAuthenticationToken(AuthRequest authRequest) {
        ApiResponse<AuthResponse> response = new ApiResponse<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        } catch (DisabledException | LockedException ex) {
            log.error(ex.getMessage());
            response.setStatus(HttpStatus.LOCKED.value());
            response.setMessage(ex.getMessage());
            return response;
        } catch (BadCredentialsException ex) {
            log.error(ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage(ex.getMessage());
            return response;
        }

        try {
            UserDetails userDetails = userService.loadUserByUsername(authRequest.getUserName());
            String token = jwtUtil.generateToken(userDetails);

            //expire previous login
            List<UserLogin> loginList = checkLoginsExist(userDetails);
            if (!loginList.isEmpty()) {
                userLoginRepository.saveAll(loginList);
            }

            //create new login with token
            userLoginRepository.save(createLogin(token, userDetails));

            User user = userRepository.findByEmail(userDetails.getUsername());
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setCookieValue(generateCookieValue(authRequest.getUserName(), authRequest.getPassword()));
            if (user.getRole() != null) {
                authResponse.setRoleResponse(roleResponseMapper(user.getRole()));
            }
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Successfully Authorized!");
            response.setObject(authResponse);
            return response;
        } catch (LsmException ex) {
            throw new LsmException(ex.getMessage());
        }
    }

    private RoleResponse roleResponseMapper(Role roleEntity) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(roleEntity.getId());
        roleResponse.setName(roleEntity.getName());
        roleResponse.setIsSuper(roleEntity.getIsSuper());

        List<ViewPermission> viewPermissionList = new ArrayList<>();
        List<ActionPermission> actionPermissionList = new ArrayList<>();
        if (roleEntity.getRolePermissionList() != null) {
            for (RolePermission rolePermission : roleEntity.getRolePermissionList()) {
                if (rolePermission.getIsActive() != null && rolePermission.getActive() != null && rolePermission.getIsActive() && rolePermission.getActive()) {
                    if (PermissionType.BTN_ACTION.equals(rolePermission.getPermission().getPermissionType())) {
                        ActionPermission actionPermission = new ActionPermission();
                        actionPermission.setId(rolePermission.getId());
                        actionPermission.setType(rolePermission.getPermission().getPermissionType());
                        actionPermission.setValue(rolePermission.getPermission().getValue());
                        actionPermission.setPermissionCategory(rolePermission.getPermission().getPermissionCategory());
                        actionPermissionList.add(actionPermission);
                    } else if (PermissionType.VIEW.equals(rolePermission.getPermission().getPermissionType())) {
                        ViewPermission viewPermission = new ViewPermission();
                        viewPermission.setId(rolePermission.getId());
                        viewPermission.setType(rolePermission.getPermission().getPermissionType());
                        viewPermission.setValue(rolePermission.getPermission().getValue());
                        viewPermission.setPermissionCategory(rolePermission.getPermission().getPermissionCategory());
                        viewPermissionList.add(viewPermission);
                    }
                }
            }
            roleResponse.setActionPermissionList(actionPermissionList);
            roleResponse.setViewPermissionList(viewPermissionList);
        }
        return roleResponse;
    }

    private UserLogin createLogin(String token, UserDetails userDetails) {
        UserLogin loginDao = new UserLogin();
        User user = userRepository.findByEmail(userDetails.getUsername());
        loginDao.setUser(user);
        loginDao.setTokenKey(token);
        loginDao.setDateLogin(new Date());
        loginDao.setDateCreated(new Date());
        loginDao.setDateUpdated(new Date());
        loginDao.setTokenExpired(!jwtUtil.validateToken(token, userDetails));
        return loginDao;
    }

    private List<UserLogin> checkLoginsExist(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new LsmException("User not found!");
        }
        List<UserLogin> userLoginDaoList = userLoginRepository.findAllByUserIdAndTokenExpired(user.getId(), false);
        if (userLoginDaoList != null && userLoginDaoList.size() > 0) {
            for (UserLogin logins : userLoginDaoList) {
                logins.setTokenExpired(true);
            }
        }
        return userLoginDaoList;
    }

    private String generateCookieValue(String username, String password) {
        return passwordEncoder.encode(username) + "." + passwordEncoder.encode(password);
    }
}
