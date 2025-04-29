package com.example.springsecurity;

import com.example.springsecurity.entity.ResearchTasks;
import com.example.springsecurity.repository.ResearchTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResearchTaskRepository researchTaskRepository;

    private Long savedTaskId;
    private final String validTaskJson = """
        {
            "title": "Test Task",
            "description": "Test Description",
            "completed": false
        }
        """;
    @BeforeEach
    void setup() {
        // Clear any existing data
        researchTaskRepository.deleteAll();

        // Create and save a test task
        ResearchTasks task = new ResearchTasks();
        task.setTitle("Existing Task");
        task.setDescription("For testing");
        task.setCompleted(false);
        ResearchTasks savedTask = researchTaskRepository.save(task);
        savedTaskId = savedTask.getId();
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanAccessAllEndpoints() throws Exception {
        // Test POST
        mockMvc.perform(post("/api/researchtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validTaskJson))
                .andExpect(status().isCreated());

        // Test DELETE using the saved task ID
        mockMvc.perform(delete("/api/researchtasks/" + savedTaskId))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "USER")
    void userCannotModifyResearchTasks() throws Exception {
        // Test POST
        mockMvc.perform(post("/api/researchtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validTaskJson))
                .andExpect(status().isForbidden());

        // Test DELETE
        mockMvc.perform(delete("/api/researchtasks/" + savedTaskId))
                .andExpect(status().isForbidden());
    }
    @Test
    void unauthenticatedUserGetsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/researchtasks"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "USER")
    void userCanCompleteResearchTask() throws Exception {
        // Use the saved task ID
        mockMvc.perform(patch("/api/researchtasks/" + savedTaskId + "/complete"))
                .andExpect(status().isOk());
    }
}