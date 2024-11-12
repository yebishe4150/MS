package com.itm.space.backendresources.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private Keycloak keycloak;

    private String userId;

    @Test
    @WithMockUser(roles = "MODERATOR")
    void createUser() throws Exception{
       UserRequest userRequest = new UserRequest(
                "testname",
                "testmail@mail.com",
                "testpassword",
                "testfirstname",
                "testlastname"
        );
        mvc.perform(requestWithContent(post("/api/users"),userRequest))
                .andExpect(status().isOk());
        userId = keycloak.realm("ITM").users().search(userRequest.getUsername()).get(0).getId();
        assertNotNull(userId, "User ID should not be null after creation");
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void getUserById()throws Exception{
             createUser();
            mvc.perform(get("/api/users/"+userId))
                .andExpect(status().isOk());
    }

    @BeforeEach
    public void upDown(){
        if (userId != null){
            keycloak.realm("ITM").users().get(userId).remove();
        }
    }

    @AfterEach
    public void tearDown(){
        if (userId != null){
            keycloak.realm("ITM").users().get(userId).remove();
        }
    }
}