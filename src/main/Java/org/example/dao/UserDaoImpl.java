package org.example.dao;

import org.example.model.Role;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserDaoImpl(@Lazy BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    // private final Map<String, User> userMap = Collections.singletonMap("username", new User("Ivan",
     //       "Ivanov", "Google", 50, 40000, "password", "username",
       //     Collections.singleton(new Role(1L, "ROLE_user"))));

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User saveUser(User user) {
        User userFromDb = getUserByName(user.getUsername());
        if(userFromDb == null) {
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER" )));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            entityManager.persist(user);
        }else{
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
        entityManager.remove(findUserById(id));
        entityManager.flush();
        entityManager.clear();

    }
    @Transactional
    @Override
    public User updateUser(User updatedUser) {
        entityManager.merge(updatedUser);
        return updatedUser;
    }

    @Override
    public List<User> usersList() {
        return entityManager.createQuery("FROM User").getResultList();
    }

    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByName(String username) {
        try {
            return (User) entityManager.
                    createQuery("from User u where u.username = :username").
                    setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

}
