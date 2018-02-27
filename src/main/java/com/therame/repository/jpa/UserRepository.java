package com.therame.repository.jpa;

import com.therame.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("userRepo")
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByConfirmationToken(String confirmationToken);

    @Query("select u from User u where upper(concat(u.firstName, ' ', u.lastName)) like upper(concat('%', :name, '%')) and u.type in :types")
    List<User> findByNameAndType(@Param("name") String name, @Param("types") List<User.Type> types);
}
