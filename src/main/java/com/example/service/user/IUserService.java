package com.example.service.user;

import com.example.model.User;
import com.example.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    User getByUsername(String username);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
