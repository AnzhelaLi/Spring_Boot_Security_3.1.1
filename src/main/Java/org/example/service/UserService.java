package org.example.service;

import org.example.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface UserService {
    User saveUser(User user);

    void deleteUser(Long id);

    User updateUser(User user);

    List<User> usersList();

    User findUserById(Long id);

    UserDetails loadUserByUsername(String username);
}
