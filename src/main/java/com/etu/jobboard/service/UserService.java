package com.etu.jobboard.service;

import com.etu.jobboard.model.User;
import com.etu.jobboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUser(User user) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        // 首先确保用户存在
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found");
        }

        // 如果修改了密码，需要重新加密
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean isEmployer(User user) {
        return user.getRole() == User.Role.EMPLOYER;
    }

    @Transactional(readOnly = true)
    public boolean isJobSeeker(User user) {
        return user.getRole() == User.Role.JOB_SEEKER;
    }

    @Transactional(readOnly = true)
    public boolean isAdmin(User user) {
        return user.getRole() == User.Role.ADMIN;
    }
}
