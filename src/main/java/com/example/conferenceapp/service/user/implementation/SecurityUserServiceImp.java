package com.example.conferenceapp.service.user.implementation;

import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.model.user.SecurityUser;
import com.example.conferenceapp.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUserServiceImp implements UserDetailsService {


    private final UserDao userRepo;

    public SecurityUserServiceImp(UserDao userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {


        Optional<User> user = userRepo.findByUsername(userName);
        user.orElseThrow
                (() -> new UsernameNotFoundException("not found" + userName));

        return user.map(SecurityUser::new).get();

    }


}
