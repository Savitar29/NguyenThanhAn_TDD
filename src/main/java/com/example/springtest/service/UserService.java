package com.example.springtest.service;

import com.example.springtest.model.User;
import com.example.springtest.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    IUserRepository userRepository;
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // return access token if successfully logged in, otherwise throws an error
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password");
        }

        return "accessToken";
    }
}
