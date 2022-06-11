package com.example.conferenceapp.service;

import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.exceptions.RecordNotFoundException;
import com.example.conferenceapp.exceptions.UserServiceException;
import com.example.conferenceapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> get() {
        return userDao.findAll();

    }

    @Override
    public String get_LoginsAndEmails() {
        List<User> users = userDao.findAll();
        return users.toString();

    }

    @Override
    public User getById(long id) {
        return userDao.findById(id).orElseThrow(() -> new RecordNotFoundException("No users with id: " + id));
    }

    @Override
    public User save(User user) throws UserServiceException {
        List<User> users = get();
        for (User u : users) {
            if (u.getLogin().equals(user.getLogin()) && !u.getEmail().equals(user.getEmail())) {
                throw new UserServiceException("Users with login: " + user.getLogin() + " exists");
            } else if (!u.getLogin().equals(user.getLogin()) && u.getEmail().equals(user.getEmail())) {
                throw new UserServiceException("Users with email: " + user.getEmail() + " exists");
            } else if (u.getLogin().equals(user.getLogin()) && u.getEmail().equals(user.getEmail())) {
                throw new UserServiceException("Users already exists");
            } else if (user.getRole() < 0 || user.getRole() > 2 || user.getLectures().size() > 5 || user.getPassword().length() < 6) {
                throw new UserServiceException("Invalid user");
            }

        }

        User user1 = new User();
        user1.setEmail(user.getEmail());
        user1.setLogin(user.getLogin());
        user1.setRole(user.getRole());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user1);
    }

    @Override
    public void delete(long id) {
        userDao.findById(id).orElseThrow(() -> new RecordNotFoundException("No users with id: " + id));
        userDao.deleteById(id);
    }

    @Override
    public User updateOrSave(long id, User user) throws UserServiceException {
        Optional<User> user1 = userDao.findById(id);
        List<User> users = get();
        for (User u : users) {
            if (u.getLogin().equals(user.getLogin()) && !u.getEmail().equals(user.getEmail())) {
                throw new UserServiceException("Users with login: " + user.getLogin() + " exists");
            } else if (!u.getLogin().equals(user.getLogin()) && u.getEmail().equals(user.getEmail())) {
                throw new UserServiceException("Users with email: " + user.getEmail() + " exists");
            } else if (u.getLogin().equals(user.getLogin()) && u.getEmail().equals(user.getEmail())) {
                throw new UserServiceException("Users already exists");
            } else if (user.getRole() < 0 || user.getRole() > 2 || user.getLectures().size() > 5 || user.getPassword().length() < 6) {
                throw new UserServiceException("Invalid user");
            }
        }
        if (user1.isPresent()) {
            User user_modified = user1.get();
            user_modified.setEmail(user.getEmail());
            user_modified.setLogin(user.getLogin());
            user_modified.setRole(user.getRole());
            user_modified.setPassword(user.getPassword());
            return userDao.save(user_modified);
        } else {
            User user_new = new User();
            user_new.setEmail(user.getEmail());
            user_new.setLogin(user.getLogin());
            user_new.setRole(user.getRole());
            user_new.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDao.save(user_new);
        }

    }

    @Override
    public User updateEmail(String old_email, String new_email) throws UserServiceException {

        Optional<User> user1 = userDao.findByemail(old_email);
        if (old_email.equals(new_email)) {
            throw new UserServiceException("Emails must be different");
        }
        List<User> users = get();
        for (User u : users) {
            if (u.getEmail().equals(new_email)) {
                throw new UserServiceException("Email exist");
            }
        }
        if (user1.isPresent()) {
            String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(new_email);
            if (matcher.matches()) {
                User user_modified = user1.get();
                user_modified.setEmail(new_email);
                return userDao.save(user_modified);
            } else {
                throw new UserServiceException("Invalid email");
            }
        } else {
            throw new RecordNotFoundException("No user with email: " + old_email);
        }
    }

}
