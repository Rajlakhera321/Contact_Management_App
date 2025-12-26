package com.scm.scm20.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.scm20.entities.User;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.helper.ResourceNotFoundException;
import com.scm.scm20.repositories.UserRepositories;
import com.scm.scm20.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void deleteUser(String id) {
        User user2 = userRepositories.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepositories.delete(user2);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositories.findAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepositories.findById(id);
    }

    @Override
    public boolean isUserExists(String id) {
        User user2 = userRepositories.findById(id)
                .orElse(null);
        return user2 != null;
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        User user = userRepositories.findByEmail(email)
                .orElse(null);
        return user != null;
    }

    @Override
    public User saveUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoleList(List.of(AppConstants.USER_ROLE));

        logger.info(user.getProvider().toString());
        return userRepositories.save(user);
    }

    @Override
    public Optional<User> updateUser(User user) {

        User user2 = userRepositories.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderId(user.getProviderId());
        User save = userRepositories.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public User getUserByEmail(String email) {

        return userRepositories.findByEmail(email).orElse(null);
    }

}
