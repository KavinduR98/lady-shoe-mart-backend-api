package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.UserDto;
import com.ushan.lady_shoe_mart.auth.domain.request.UserPrincipal;
import com.ushan.lady_shoe_mart.auth.entity.Role;
import com.ushan.lady_shoe_mart.auth.entity.User;
import com.ushan.lady_shoe_mart.auth.repository.RoleRepository;
import com.ushan.lady_shoe_mart.auth.repository.UserRepository;
import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public ApiResponse<UserDto> save(UserDto user) {
        ApiResponse<UserDto> response = new ApiResponse<>();
        if (user.getEmail() == null) {
            throw new LsmException("Email can't empty! ");
        }
        if (user.getPassword() == null) {
            throw new LsmException("Password can't be empty!");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new LsmException("User email already exist!");
        }
        User userDao = modelMapper.map(user, User.class);
        userDao.setEmail(user.getEmail());
        if (user.getRoleId() != null) {
            Role role = roleRepository.findById(user.getRoleId().longValue())
                    .orElseThrow(() -> new LsmException("Role not found"));
            userDao.setRole(role);
        }
        userDao.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.setActive(true);
        userDao.setDateCreated(new Date());
        userDao.setDateUpdated(new Date());
        userDao.setIsActive(true);
        userRepository.save(userDao);
        response.setObject(modelMapper.map(userDao, UserDto.class));
        response.setMessage("User created successfully!");
        response.setStatus(HttpStatus.CREATED.value());
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAllUser() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(c -> modelMapper.map(c, UserDto.class)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ApiResponse<UserDto> update(UserDto user) {
        ApiResponse<UserDto> response = new ApiResponse<>();
        if (user.getId() == null) {
            throw new LsmException("User Id empty!");
        }
        User userDao = userRepository.findById(user.getId())
                                        .orElseThrow(() -> new LsmException("User not found!"));
        if (!userDao.getEmail().equals(user.getEmail())) {
            throw new LsmException("You can't change the username!");
        }
        if ((userDao.getRole() != null && userDao.getRole().getId() != user.getRoleId().longValue()) || (userDao.getRole() == null && user.getRoleId() != null)) {
            if (user.getRoleId() != null) {
                Role role = roleRepository.findById(user.getRoleId().longValue()).orElseThrow(() -> new LsmException("Role not found!"));
                userDao.setRole(role);
            } else {
                userDao.setRole(null);
            }
        }
        modelMapper.map(user, User.class);
        userDao.setDateUpdated(new Date());
        userRepository.save(userDao);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("User updated successfully!");
        response.setObject(modelMapper.map(userDao, UserDto.class));
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user != null) {
            return new UserPrincipal(user);
        }
        throw new UsernameNotFoundException("User not found with the name" + username);
    }
}
