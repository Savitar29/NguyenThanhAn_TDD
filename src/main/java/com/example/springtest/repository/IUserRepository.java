package com.example.springtest.repository;

import com.example.springtest.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository {
    User findByEmail(String mail);

    User save(User any);
}
