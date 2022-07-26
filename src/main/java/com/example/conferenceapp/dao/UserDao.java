package com.example.conferenceapp.dao;


import com.example.conferenceapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String login);

    Optional<User> findByEmail(String email);
}
