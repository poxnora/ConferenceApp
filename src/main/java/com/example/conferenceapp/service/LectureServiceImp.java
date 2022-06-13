package com.example.conferenceapp.service;

import com.example.conferenceapp.dao.LectureDao;
import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.exceptions.LectureServiceException;
import com.example.conferenceapp.exceptions.RecordNotFoundException;
import com.example.conferenceapp.exceptions.UserServiceException;
import com.example.conferenceapp.helper.toFile;
import com.example.conferenceapp.model.Lecture;
import com.example.conferenceapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LectureServiceImp implements LectureService {


    private final LectureDao lectureDao;

    public LectureServiceImp(LectureDao lectureDao, UserDao userDao) {
        this.lectureDao = lectureDao;
        this.userDao = userDao;
    }


    private final UserDao userDao;

    @Override
    public List<Lecture> get() {
        return lectureDao.findAll();
    }

    @Override
    public String get_plan() {
        List<Lecture> lectures = get();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Lecture l : lectures) {
            sb.append(l.toString());
        }
        sb.append("{}]");
        return sb.toString();
    }

    @Override
    public Lecture getById(long id) {
        return lectureDao.findById(id).orElseThrow(() -> new RecordNotFoundException("No lectures with id: " + id));
    }

    @Override
    public Lecture save(Lecture lecture) throws LectureServiceException {

        List<Lecture> lectures = lectureDao.findAll();
        if (lecture.getTheme() < 1 || lecture.getTheme() > 3)
            throw new LectureServiceException("Invalid theme");
        int count = 0;
        for (Lecture l : lectures) {
            if (l.getTheme() == lecture.getTheme()) {
                count += 1;
                if (count > 2) {
                    throw new LectureServiceException("Maximum lectures reached");
                }
            }
        }
        return lectureDao.save(lecture);
    }

    @Override
    public void delete(long id) {
        lectureDao.findById(id).orElseThrow(() -> new RecordNotFoundException("No lectures with id: " + id));
        lectureDao.deleteById(id);
    }

    @Override
    public Lecture updateOrSave(long id, Lecture lecture) throws LectureServiceException {
        Optional<Lecture> lectureOptional = lectureDao.findById(id);
        List<Lecture> lectures = get();
        if (lecture.getTheme() < 1 || lecture.getTheme() > 3)
            throw new LectureServiceException("Invalid theme");
        int count = 0;
        if (lectureOptional.isPresent()) {
            Lecture lecture_modified = lectureOptional.get();

            for (Lecture l : lectures) {

                if (l.getTheme() == lecture.getTheme()) {
                    count += 1;
                    if (count > 2) {
                        throw new LectureServiceException("Maximum lectures reached");
                    }
                }
            }
            lecture_modified.setParticipants(lecture.getParticipants());
            lecture_modified.setTheme(lecture.getTheme());
            lecture_modified.setTitle(lecture.getTitle());
            lecture_modified.setStarts(lecture.getStarts());
            return lectureDao.save(lecture_modified);
        } else {
            Lecture lecture_new = new Lecture();
            for (Lecture l : lectures) {

                if (l.getTheme() == lecture.getTheme()) {
                    count += 1;
                    if (count > 2) {
                        throw new LectureServiceException("Maximum lectures reached");
                    }
                }
            }

            lecture_new.setParticipants(lecture.getParticipants());
            lecture_new.setTheme(lecture.getTheme());
            lecture_new.setTitle(lecture.getTitle());
            lecture_new.setStarts(lecture.getStarts());
            return lectureDao.save(lecture_new);
        }
    }


    @Override
    public Lecture addUser(long id, String login, String email) throws LectureServiceException, UserServiceException, IOException {
        Optional<Lecture> lectureOptional = lectureDao.findById(id);
        if (lectureOptional.isPresent()) {
            Lecture lecture_modified = lectureOptional.get();
            if (lecture_modified.getParticipants().size() < 5) {
                Optional<User> user_email = userDao.findByemail(email);
                Optional<User> user_login = userDao.findByusername(login);
                if (user_email.isPresent() && user_login.isPresent()) {
                    User user_modified = user_email.get();
                    User user2 = user_login.get();
                    if (user_modified == user2) {
                        for (Lecture l : user_modified.getLectures()) {
                            if (l.getStarts() == lecture_modified.getStarts())
                                throw new LectureServiceException("Cannot enroll, you've already joined lecture at this hour");
                        }
                        Set<User> new_participants = lecture_modified.getParticipants();
                        Set<Lecture> new_lectures = user_modified.getLectures();
                        new_participants.add(user_modified);
                        new_lectures.add(lecture_modified);
                        lecture_modified.setParticipants(new_participants);
                        user_modified.setLectures(new_lectures);
                        toFile.saveFile("powiadomienia.txt", user_modified.getEmail() + " Succesful reservation " + lecture_modified.getTitle());
                        return lectureDao.save(lecture_modified);

                    } else {
                        throw new UserServiceException("Invalid login or email");
                    }
                } else {
                    throw new UserServiceException("Invalid login or email");
                }
            } else {
                throw new LectureServiceException("Lecture with id: " + id + " is full");
            }
        } else {
            throw new RecordNotFoundException("No lecture with id: " + id);
        }

    }

    @Override
    public String getLectures(String login) throws UserServiceException {
        Optional<User> user = userDao.findByusername(login);
        if (user.isPresent()) {
            User user1 = user.get();
            Set<Lecture> lectures = user1.getLectures();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Lecture l : lectures) {
                sb.append("{" + "\"title\":" + "\"").append(l.getTitle()).append("\"").append("},");
            }
            sb.append("{}]");
            return sb.toString();
        } else {
            throw new UserServiceException("No users with login: " + login);
        }
    }

    @Override
    public Lecture cancelUser(long id, String login, String email) throws LectureServiceException, UserServiceException {
        Optional<Lecture> lectureOptional = lectureDao.findById(id);
        if (lectureOptional.isPresent()) {
            Lecture lecture_modified = lectureOptional.get();
            Optional<User> user_email = userDao.findByemail(email);
            Optional<User> user_login = userDao.findByusername(login);
            if (user_email.isPresent() && user_login.isPresent()) {
                User user1 = user_email.get();
                User user2 = user_login.get();
                if (user1 == user2) {
                    for (Lecture l : user1.getLectures()) {
                        if (l == lecture_modified) {
                            Set<Lecture> new_lectures = user1.getLectures();
                            new_lectures.remove(l);
                            Set<User> new_participants = lecture_modified.getParticipants();
                            new_participants.remove(user1);
                            lecture_modified.setParticipants(new_participants);
                            user1.setLectures(new_lectures);
                            return lectureDao.save(lecture_modified);
                        }
                    }
                    throw new LectureServiceException("User with login: " + login + " didn't join lecture with id: " + id);
                } else {
                    throw new UserServiceException("Invalid login or email");
                }
            }
            throw new UserServiceException("Invalid login or email");

        } else {
            throw new RecordNotFoundException("No lecture with id: " + id);
        }

    }

    public String lecturePopularity() {
        List<Lecture> lectures = lectureDao.findAll();
        StringBuilder sb = new StringBuilder();
        int lecture_number = 1;
        sb.append("[");
        for (Lecture l : lectures) {

            sb.append("{\"Lecture\": ");
            sb.append(lecture_number);
            sb.append(",");
            sb.append(" \"Popularity\": ");
            sb.append(((float) l.getParticipants().size() / 15) * 100);
            lecture_number += 1;
            sb.append("},");


        }
        sb.append("{}]");
        return sb.toString();
    }

    public String themePopularity() {
        List<Lecture> lectures = lectureDao.findAll();
        StringBuilder sb = new StringBuilder();
        int users_number = 0;
        sb.append("[");
        for (int i = 1; i <= 3; i++) {
            sb.append("{\"Theme\": ");
            sb.append(i);
            sb.append(",");
            sb.append(" \"Popularity\": ");
            for (Lecture l : lectures) {
                if (l.getTheme() == i) {
                    users_number += l.getParticipants().size();

                }

            }
            sb.append((float) users_number / 15 * 100);
            users_number = 0;
            sb.append("},");


        }
        sb.append("{}]");
        return sb.toString();
    }


}
