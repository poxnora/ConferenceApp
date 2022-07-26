package com.example.conferenceapp.service.lecture.implementation;

import com.example.conferenceapp.dao.LectureDao;
import com.example.conferenceapp.dao.UserDao;
import com.example.conferenceapp.exception.RecordNotFoundException;
import com.example.conferenceapp.exception.RecordNotSavedException;
import com.example.conferenceapp.helper.ToFile;
import com.example.conferenceapp.model.Conference;
import com.example.conferenceapp.model.Lecture;
import com.example.conferenceapp.model.user.User;
import com.example.conferenceapp.service.lecture.LectureService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class LectureServiceImp implements LectureService {


    private final LectureDao lectureDao;
    private final UserDao userDao;


    public LectureServiceImp(LectureDao lectureDao, UserDao userDao) {
        this.lectureDao = lectureDao;
        this.userDao = userDao;
    }

    @Override
    public List<Lecture> get() {
        return lectureDao.findAll();
    }

    @Override
    public String getPlan() {
        List<Lecture> lectures = get();
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < lectures.size(); i++) {
            sb.append(lectures.get(i).toString());
            if (i == lectures.size() - 1) {
                sb.append("]");
            } else {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Override
    public Lecture getById(Long id) {
        return lectureDao.findById(id).orElseThrow(() -> new RecordNotFoundException("No lectures with id: " + id));
    }

    @Override
    public Lecture save(Lecture lecture) {

        List<Lecture> lectures = lectureDao.findAll();
        if (lecture.getTheme() < 1 || lecture.getTheme() > Conference.getThemes())
            throw new RecordNotSavedException("Invalid theme");
        int count = 0;
        for (Lecture l : lectures) {
            if (Objects.equals(l.getTheme(), lecture.getTheme())) {

                if (count > Conference.getThemes()) {
                    throw new RecordNotSavedException("Maximum lectures with that theme reached");
                }
                count += 1;
            }
        }
        return lectureDao.save(lecture);
    }

    @Override
    public Lecture updateOrSave(Long id, Lecture lecture) {
        Optional<Lecture> lectureOptional = lectureDao.findById(id);
        List<Lecture> lectures = lectureDao.findAll();
        if (lecture.getTheme() < 1 || lecture.getTheme() > Conference.getThemes())
            throw new RecordNotSavedException("Invalid theme");
        int count = 0;
        if (lectureOptional.isPresent()) {
            Lecture lecture_modified = lectureOptional.get();
            for (Lecture l : lectures) {
                if (Objects.equals(l.getTheme(), lecture.getTheme())) {

                    if (count > Conference.getThemes()) {
                        throw new RecordNotSavedException("Maximum lectures with that theme reached");
                    }
                    count += 1;
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
                if (Objects.equals(l.getTheme(), lecture.getTheme())) {

                    if (count > Conference.getThemes()) {
                        throw new RecordNotSavedException("Maximum lectures with that theme reached");
                    }
                    count += 1;
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
    public Lecture addUser(Long id, String login, String email) throws IOException {
        Optional<Lecture> lectureOptional = lectureDao.findById(id);
        if (lectureOptional.isPresent()) {
            Lecture lecture_modified = lectureOptional.get();
            if (lecture_modified.getParticipants().size() < Conference.getParticipants_per_lecture()) {
                Optional<User> user_email = userDao.findByEmail(email);
                Optional<User> user_login = userDao.findByUsername(login);
                if (user_email.isPresent() && user_login.isPresent()) {
                    User user_modified = user_email.get();
                    User user2 = user_login.get();
                    if (user_modified == user2) {
                        for (Lecture l : user_modified.getLectures()) {
                            if (Objects.equals(l.getStarts(), lecture_modified.getStarts()))
                                throw new RecordNotSavedException("Cannot enroll, you've already joined lecture at this hour");
                        }
                        Set<User> new_participants = lecture_modified.getParticipants();
                        List<Lecture> new_lectures = user_modified.getLectures();
                        new_participants.add(user_modified);
                        new_lectures.add(lecture_modified);
                        lecture_modified.setParticipants(new_participants);
                        user_modified.setLectures(new_lectures);
                        ToFile.saveFile("powiadomienia.txt", user_modified.getEmail() + " Succesful reservation " + lecture_modified.getTitle());
                        return lectureDao.save(lecture_modified);

                    } else {
                        throw new RecordNotSavedException("Invalid login or email");
                    }
                } else {
                    throw new RecordNotSavedException("Invalid login or email");
                }
            } else {
                throw new RecordNotSavedException("Lecture with id: " + id + " is full");
            }
        } else {
            throw new RecordNotFoundException("No lecture with id: " + id);
        }

    }


    @Override
    public Lecture cancelUser(Long id, String login, String email) {
        Optional<Lecture> lectureOptional = lectureDao.findById(id);
        if (lectureOptional.isPresent()) {
            Lecture lecture_modified = lectureOptional.get();
            Optional<User> user_email = userDao.findByEmail(email);
            Optional<User> user_login = userDao.findByUsername(login);
            if (user_email.isPresent() && user_login.isPresent()) {
                if (user_email.get().equals(user_login.get())) {
                    User user = user_email.get();
                    for (Lecture l : user.getLectures()) {
                        if (l.equals(lecture_modified)) {
                            List<Lecture> new_lectures = user.getLectures();
                            new_lectures.remove(l);
                            Set<User> new_participants = lecture_modified.getParticipants();
                            new_participants.remove(user);
                            lecture_modified.setParticipants(new_participants);
                            user.setLectures(new_lectures);
                            return lectureDao.save(lecture_modified);
                        }
                    }
                    throw new RecordNotSavedException("User with login: " + login + " didn't join lecture with id: " + id);
                } else {
                    throw new RecordNotSavedException("Invalid login or email");
                }
            }
            throw new RecordNotSavedException("Invalid login or email");

        } else {
            throw new RecordNotFoundException("No lecture with id: " + id);
        }

    }

    public String lecturePopularity() {
        List<Lecture> lectures = lectureDao.findAll();
        StringBuilder sb = new StringBuilder();
        int lecture_number = 1;
        sb.append("[");
        for (int i = 0; i < lectures.size(); i++) {

            sb.append("{\"Lecture\": ");
            sb.append(lecture_number);
            sb.append(",");
            sb.append(" \"Popularity\": ");
            sb.append(((float) lectures.get(i).getParticipants().size() / 15) * 100);
            lecture_number += 1;
            if (i != lectures.size() - 1)
                sb.append("},");
            else
                sb.append("}]");
        }
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
            if (i != 3)
                sb.append("},");
            else
                sb.append("}]");
        }
        return sb.toString();
    }


}
