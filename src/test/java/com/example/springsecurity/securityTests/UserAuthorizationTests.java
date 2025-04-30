package com.example.springsecurity.securityTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.springsecurity.entity.ResearchTasks;
import com.example.springsecurity.repository.ResearchTaskRepository;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UserAuthorizationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResearchTaskRepository researchTaskRepository;

    private Long existingTaskId;

    @BeforeEach
    void setUp() {
        // Clear and create test data with all required fields
        researchTaskRepository.deleteAll();

        ResearchTasks task = new ResearchTasks();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        ResearchTasks savedTask = researchTaskRepository.save(task);
        existingTaskId = savedTask.getId();
    }
    @Test
    @WithMockUser(roles = "USER")
    void userShouldAccessPermittedEndpoints() throws Exception {
        mockMvc.perform(get("/api/researchtasks/" + existingTaskId))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/researchtasks/" + existingTaskId + "/complete"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "USER")
    void userShouldNotAccessAdminEndpoints() throws Exception {
        mockMvc.perform(delete("/api/researchtasks/" + existingTaskId))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/researchtasks"))
                .andExpect(status().isForbidden());
    }
}
