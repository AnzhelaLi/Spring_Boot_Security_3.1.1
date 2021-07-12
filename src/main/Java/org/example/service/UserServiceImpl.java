package org.example.service;

import org.example.dao.RoleDao;
import org.example.dao.UserDao;
import org.example.model.Role;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDao userDao;
    @Autowired
    public UserServiceImpl(@Lazy UserDao userDao) {
        this.userDao = userDao;
    }

    //public UserServiceImpl() {

    //}

    /* @Autowired
     private RoleDao roleDao;
 */
    @Transactional
    @Override
    public User saveUser(User user) {
        return userDao.saveUser(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Transactional
    @Override
    public User updateUser(User user) {

        return userDao.updateUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> usersList() {
        return userDao.usersList();
    }

    @Transactional
    @Override
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByName(username);
        if(user == null ) {
            throw new UsernameNotFoundException("username not found");
        }
     Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole())  );
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                grantedAuthorities);

    }
}
