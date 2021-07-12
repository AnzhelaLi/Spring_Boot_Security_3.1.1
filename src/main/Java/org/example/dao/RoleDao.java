package org.example.dao;

import org.example.model.Role;

import javax.persistence.EntityManager;

public interface RoleDao {
   Role findByRole (String role);
}
