package com.mongotest.MonGo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongotest.MonGo.model.User;
import com.mongotest.MonGo.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public String createUser(User user) {
        userRepo.save(user);
        return "ok";
    }

    public Optional<User> searchUser(String email) {
        return userRepo.findByEmail(email);
    }
}
