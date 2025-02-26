package com.test.magical_grass.repositories;

import com.test.magical_grass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username =:username")
    User findByUsername(@Param("username") String username);
}
