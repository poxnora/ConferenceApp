package com.example.conferenceapp.dao;

import com.example.conferenceapp.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureDao extends JpaRepository<Lecture, Long> {
}
