package com.example.conferenceapp;

import com.example.conferenceapp.model.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestLecture extends TestAbstract{

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test()
    public void getLecturesList() throws Exception {
        String uri = "/lectures/";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

    }

    @Test
    public void addLectures() throws Exception {
        String uri = "/users/add";
        Lecture lecture = new Lecture();
        lecture.setTitle("lecture");
        lecture.setTheme(1);
        lecture.setStarts(1);

        String inputJson = super.mapToJson(lecture);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);

    }

    @Test
    public void updateLectures() throws Exception {
        String uri = "/users/add/2";
        Lecture lecture = new Lecture();
        lecture.setTitle("lecture");
        lecture.setTheme(1);
        lecture.setStarts(1);

        String inputJson = super.mapToJson(lecture);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);

    }

    @Test
    public void deleteLectures() throws Exception {
        String uri = "/users/delete/2";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Deleted successfully");
    }
}


