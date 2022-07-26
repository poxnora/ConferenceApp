package com.example.conferenceapp.model.user;

import com.example.conferenceapp.dao.UserDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class UserCreator {


    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    public UserCreator(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        String password = passwordEncoder.encode("password1");
        userDao.save(new User("user1", "user1@email.com", password, "USER"));
        password = passwordEncoder.encode("password2");
        userDao.save(new User("user2", "user2@email.com", password, "USER"));
        password = passwordEncoder.encode("password3");
        userDao.save(new User("user3", "user3@email.com", password, "USER"));
        password = passwordEncoder.encode("password4");
        userDao.save(new User("user4", "user4@email.com", password, "USER"));
        password = passwordEncoder.encode("password5");
        userDao.save(new User("user5", "user5@email.com", password, "USER"));
        password = passwordEncoder.encode("password6");
        userDao.save(new User("user6", "user6@email.com", password, "USER"));
        password = passwordEncoder.encode("password7");
        userDao.save(new User("user7", "user7@email.com", password, "USER"));
        password = passwordEncoder.encode("password8");
        userDao.save(new User("user8", "user8@email.com", password, "USER"));
        password = passwordEncoder.encode("password9");
        userDao.save(new User("user9", "user9@email.com", password, "USER"));
        password = passwordEncoder.encode("password10");
        userDao.save(new User("user10", "user10@email.com", password, "USER"));
        password = passwordEncoder.encode("password11");
        userDao.save(new User("user11", "user11@email.com", password, "USER"));
        password = passwordEncoder.encode("admin");
        userDao.save(new User("admin", "admin@email.com", password, "ADMIN"));
        password = passwordEncoder.encode("prof");
        userDao.save(new User("prof", "prof@email.com", password, "PROF"));
    }
}