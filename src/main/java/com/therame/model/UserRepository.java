package com.therame.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.therame.model.User;

@Repository("userRepo")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
