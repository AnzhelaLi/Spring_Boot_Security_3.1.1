package org.example.service;

import org.example.dao.RoleDao;
import org.example.dao.UserDao;
import org.example.model.Role;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {


    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public void setUserDao(UserDao userDao) {

        this.userDao = userDao;

    }

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public UserServiceImpl() {

    }

    @Override
    public User justSaveUser(User user) {
        return userDao.justSaveUser(user);
    }


    @Override
    public User registerUser(User user) {
        User userFromDb = userDao.getUserByName(user.getUsername());
        Role role = roleDao.findByRole("ROLE_USER");
        if (userFromDb == null) {
            user.setRoles(new HashSet<>(Arrays.asList(role)));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.justSaveUser(user);
        } else {
            try {
                throw new Exception("User already exists");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public User updateUser(User user) {

        user.setRoles(new HashSet<>(Arrays.asList(roleDao.findByRole("ROLE_USER"))));
        user.setPassword(user.getPassword());
        return userDao.updateUser(user);
    }

    @Override
    public List<User> usersList() {
        return userDao.usersList();
    }

    @Override
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("username not found");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                grantedAuthorities);
    }

    /*@Override
    public void addInitData() {

        User admin = new User("Ivan", "Ivanov", "Kinopark", 33, 64000, "ivan", "$2a$10$ZBQEot6lXLJUsZlZOGJk/OHYdlAfD8Mf4ES48NgfTWhrKm1JGI7Cm");
        User user = new User("Ivana", "Ivanova", "Kinopark", 36, 60000, "ivanka", "$2a$10$yLyghRcZPTYfGCFME.Gw3OgISFBr1UFzQjTpxGKXKX6Abdg/CYZwW");

        userDao.justSaveUser(admin);
        userDao.justSaveUser(user);

        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        roleDao.saveRole(roleAdmin);
        roleDao.saveRole(roleUser);

        admin.addRole(roleAdmin);
        admin.addRole(roleUser);
        user.addRole(roleUser);

        roleAdmin.addUser(admin);
        roleUser.addUser(admin);
        roleUser.addUser(user);

    }*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        UserServiceImpl us = new UserServiceImpl();
        System.out.println(us.getPasswordEncoder().encode("passw"));
        System.out.println(us.passwordEncoder.encode("parol"));
    }
}
