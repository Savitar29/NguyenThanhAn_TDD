package com.example.springtest.service;

import com.example.springtest.model.User;
import com.example.springtest.repository.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserService {
    IUserRepository userRepository;
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkLoginStatus() {
        HttpSession session = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getSession(false);

        if (session == null) return false;

        session.setMaxInactiveInterval(30 * 60);

        return session.getAttribute("loggedInUser") != null;
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
