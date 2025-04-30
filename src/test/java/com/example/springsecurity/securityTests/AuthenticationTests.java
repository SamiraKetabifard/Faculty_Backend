package com.example.springsecurity.securityTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedAccessToProtectedEndpointsShouldFail() throws Exception {
        mockMvc.perform(get("/api/researchtasks"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/professors"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/faculties")).
                andExpect(status().isUnauthorized());
    }
}