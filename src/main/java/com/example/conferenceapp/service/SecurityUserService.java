package com.example.conferenceapp.service;

import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.model.SecurityUser;
import com.example.conferenceapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecurityUserService implements UserDetailsService{


    @Autowired
    UserDao userRepo;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {


        Optional<User> user =	userRepo.findByusername(userName);
        user.orElseThrow
                (()->new UsernameNotFoundException("not found"+userName));

        return user.map(SecurityUser:: new).get();

    }


}
