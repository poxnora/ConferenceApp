package com.example.conferenceapp.service.user.implementation;

import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.exception.RecordNotFoundException;
import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.helper.EmailRegex;
import com.example.conferenceapp.model.Conference;
import com.example.conferenceapp.model.Lecture;
import com.example.conferenceapp.model.user.User;
import com.example.conferenceapp.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {


    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImp(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> get() {
        return userDao.findAll();

    }

    @Override
    public String getLoginsAndEmailsList() {
        List<User> users = userDao.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < users.size(); i++) {
            sb.append(users.get(i).toString());
            if (i == users.size() - 1)
                sb.append(",");
            else
                sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public String getLoginAndEmail(Long id) {
        return userDao.findById(id).map(User::toString).orElseThrow(() -> new RecordNotFoundException("No users with id: " + id));
    }

    @Override
    public User getById(Long id) {
        return userDao.findById(id).orElseThrow(() -> new RecordNotFoundException("No users with id: " + id));
    }

    @Override
    public User save(User user) {
        List<User> users = userDao.findAll();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail())) {
                throw new RecordNotSavedException("User already exists");
            } else if (user.getLectures().size() > Conference.getParticipants_per_lecture() || user.getPassword().length() < 6) {
                throw new RecordNotSavedException("Invalid user details");
            }
        }
        if (!EmailRegex.matchEmail(user.getEmail()))
            throw new RecordNotSavedException("Invalid email");


        User user1 = new User();
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        user1.setAuthority(user.getAuthority());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user1);
    }


    @Override
    public User updateOrSave(Long id, User user) {
        Optional<User> user1 = userDao.findById(id);
        List<User> users = userDao.findAll();
        for (User u : users) {

            if (u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail())) {
                throw new RecordNotSavedException("User already exists");
            } else if (user.getLectures().size() > Conference.getParticipants_per_lecture() || user.getPassword().length() < 6) {
                throw new RecordNotSavedException("Invalid user details");
            }
        }

        if (!EmailRegex.matchEmail(user.getEmail()))
            throw new RecordNotSavedException("Invalid email");

        if (user1.isPresent()) {
            User user_modified = user1.get();
            user_modified.setEmail(user.getEmail());
            user_modified.setUsername(user.getUsername());
            user_modified.setAuthority(user.getAuthority());
            user_modified.setPassword(user.getPassword());
            return userDao.save(user_modified);
        } else {
            User user_new = new User();
            user_new.setEmail(user.getEmail());
            user_new.setUsername(user.getUsername());
            user_new.setAuthority(user.getAuthority());
            user_new.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDao.save(user_new);
        }


    }


    @Override
    public String getLectures(String login) {
        Optional<User> user = userDao.findByUsername(login);
        if (user.isPresent()) {
            User user1 = user.get();
            List<Lecture> lectures = user1.getLectures();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < lectures.size(); i++) {
                sb.append("{" + "\"title\":" + "\"").append(lectures.get(i).getTitle()).append("\"").append("}");
                if (i == lectures.size() - 1)
                    sb.append("]");
                else
                    sb.append((","));
            }

            return sb.toString();
        } else {
            throw new RecordNotSavedException("No users with login: " + login);
        }
    }

    @Override
    public User updateEmail(String old_email, String new_email) {
        if (old_email.equals(new_email)) {
            throw new RecordNotSavedException("Emails must be different");
        }
        List<User> users = userDao.findAll();
        for (User u : users) {
            if (u.getEmail().equals(new_email)) {
                throw new RecordNotSavedException("Email exists");
            }
        }
        Optional<User> user1 = userDao.findByEmail(old_email);
        if (user1.isPresent()) {
            if (!EmailRegex.matchEmail(new_email))
                throw new RecordNotSavedException("Invalid email");
            User user_modified = user1.get();
            user_modified.setEmail(new_email);
            return userDao.save(user_modified);
        } else {
            throw new RecordNotFoundException("No user with email: " + old_email);
        }
    }

}
