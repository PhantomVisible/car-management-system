package com.carmanagement.auth.infrastructure.repositories;

import com.carmanagement.auth.domain.models.User;
import com.carmanagement.auth.domain.ports.UserRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryAdapter extends JpaRepository<User, Long>, UserRepositoryPort {

    @Override
    Optional<User> findByEmail(String email);

    @Override
    default boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}