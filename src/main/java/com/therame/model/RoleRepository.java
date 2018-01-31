package com.therame.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.therame.model.Role;

@Repository("roleRepo")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
}
