package com.backendsem4.backend.repository;

import com.backendsem4.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DongTHD
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	Optional<User> findByNameOrEmail(String name, String email);
	Optional<User> findByName(String name);
	Boolean existsByName(String name);
	Boolean existsByEmail(String email);

}
