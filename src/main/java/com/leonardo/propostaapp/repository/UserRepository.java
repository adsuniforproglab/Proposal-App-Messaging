package com.leonardo.propostaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leonardo.propostaapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
