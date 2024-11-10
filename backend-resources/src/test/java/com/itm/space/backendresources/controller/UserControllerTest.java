package com.itm.space.backendresources.controller;

import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import com.itm.space.backendresources.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseIntegrationTest {

    @MockBean
    private UserServiceImpl userServiceImpl;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser (roles = "MODERATOR")
    void create() throws Exception{
        UserRequest userRequest = new UserRequest(
                "testname",
                "testmail@ex.com",
                "testpassword",
                "testname",
                "testlastname"
        );
        mockMvc.perform(requestWithContent(post("/api/users"),userRequest))
                .andExpect(status().isOk());
        verify(userServiceImpl).createUser(any(UserRequest.class));
    }

    @Test
    @WithMockUser (roles = "MODERATOR")
    void getUserById() throws Exception{
        UUID testId= UUID.randomUUID();
        UserResponse userResponse = new UserResponse(
                "testname",
                "testlastname",
                "testmeil@ex@.com",
                List.of(),
                List.of()
        );
        mvc.perform(get("/api/users/"+testId))
                .andExpect(status().isOk());
        verify(userServiceImpl).getUserById(any(UUID.class));
    }

    @Test
    @WithMockUser (roles = "MODERATOR")
    void hello() throws Exception{
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk());
    }
}