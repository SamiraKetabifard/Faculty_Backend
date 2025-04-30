package com.example.springsecurity.securityTests;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AdminAuthorizationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldAccessAllEndpoints() throws Exception {
        // test: admin can or cannot create task
        mockMvc.perform(post("/api/researchtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test\", \"description\":\"Test description\", \"completed\": true}"))
                .andExpect(status().isCreated());
    }
}