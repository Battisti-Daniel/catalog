package com.daniel.catalog.repositories;

import com.daniel.catalog.entities.Role;
import com.daniel.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
