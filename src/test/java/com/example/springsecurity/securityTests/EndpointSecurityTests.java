package com.example.springsecurity.securityTests;

import com.example.springsecurity.entity.ResearchTasks;
import com.example.springsecurity.repository.ResearchTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class EndpointSecurityTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResearchTaskRepository repository;

    private Long taskId;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        ResearchTasks task = new ResearchTasks();
        task.setTitle("Test");
        task.setDescription("Test");
        task.setCompleted(false);
        taskId = repository.save(task).getId();
    }
    @Test
    @WithMockUser(roles = "USER")
    void researchTaskCompletionShouldAllowUser() throws Exception {
        // Use the real saved task ID
        mockMvc.perform(patch("/api/researchtasks/" + taskId + "/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void researchTaskCompletionShouldAllowADMIN() throws Exception {
        mockMvc.perform(patch("/api/researchtasks/" + taskId + "/complete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "USER")
    void researchTaskDeletionShouldForbidUser() throws Exception {
        mockMvc.perform(delete("/api/researchtasks/1"))
                .andExpect(status().isForbidden());
    }
}