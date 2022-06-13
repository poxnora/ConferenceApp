package com.example.conferenceapp;

import com.example.conferenceapp.model.User;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestUser extends TestAbstract {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test()
    public void getUserList() throws Exception {
        String uri = "/users/";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        User[] userList = super.mapFromJson(content, User[].class);
        assertTrue(userList.length > 0);
    }

    @Test
    public void addUser() throws Exception {
        String uri = "/users/add";
        User user = new User();
        user.setEmail("user@email.com");
        user.setUsername("user");
        user.setPassword("password");
        user.setAuthority("USER");

        String inputJson = super.mapToJson(user);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(content);
        content = json.getString("email");
        assertEquals(content, user.getEmail());
    }

    @Test
    public void updateUser() throws Exception {
        String uri = "/users/add/2";
        User user = new User();
        user.setEmail("user@email.com");
        user.setUsername("user123123123");
        user.setPassword("password");
        user.setAuthority("USER");

        String inputJson = super.mapToJson(user);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(content);
        content = json.getString("email");
        assertEquals(content, user.getEmail());
    }

    @Test
    public void deleteUser() throws Exception {
        String uri = "/users/delete/2";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Deleted successfully");
    }
}
