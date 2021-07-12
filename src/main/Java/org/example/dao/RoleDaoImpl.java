package org.example.dao;

import org.example.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    private RoleDao roleDao;


    @Override
    public Role findByRole(String role) {
        return entityManager.find(Role.class, role);
    }
}
