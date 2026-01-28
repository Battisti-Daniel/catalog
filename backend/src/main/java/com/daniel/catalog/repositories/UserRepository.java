package com.daniel.catalog.repositories;

import com.daniel.catalog.entities.Product;
import com.daniel.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
