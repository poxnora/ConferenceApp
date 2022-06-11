package com.example.conferenceapp.dao;


import com.example.conferenceapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByusername(String login);

    Optional<User> findByemail(String email);
}
