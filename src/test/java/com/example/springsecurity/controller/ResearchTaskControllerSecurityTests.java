package com.example.springsecurity.controller;

import com.example.springsecurity.entity.ResearchTasks;
import com.example.springsecurity.repository.ResearchTaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ResearchTaskControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResearchTaskRepository researchTaskRepository;

    private Long taskId;

    @BeforeEach
    void setUp() {
        researchTaskRepository.deleteAll();

        ResearchTasks task = new ResearchTasks();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        ResearchTasks savedTask = researchTaskRepository.save(task);
        taskId = savedTask.getId();
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void createResearchTask_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(post("/api/researchtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Task\",\"description\":\"New Description\",\"completed\":false}"))
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllResearchTasks_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(get("/api/researchtasks"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getResearchTaskById_ShouldAllowAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/researchtasks/" + taskId))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateResearchTask_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(put("/api/researchtasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"completed\":true}"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteResearchTask_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(delete("/api/researchtasks/" + taskId))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void completeResearchTask_ShouldAllowAuthenticatedUsers() throws Exception {
        mockMvc.perform(patch("/api/researchtasks/" + taskId + "/complete"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void inCompleteResearchTask_ShouldAllowAuthenticatedUsers() throws Exception {
        mockMvc.perform(patch("/api/researchtasks/" + taskId + "/in-complete"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "USER")
    void createResearchTask_ShouldForbidUser() throws Exception {
        mockMvc.perform(post("/api/researchtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Task\",\"description\":\"New Description\",\"completed\":false}"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "USER")
    void getAllResearchTasks_ShouldForbidUser() throws Exception {
        mockMvc.perform(get("/api/researchtasks"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "USER")
    void updateResearchTask_ShouldForbidUser() throws Exception {
        mockMvc.perform(put("/api/researchtasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"completed\":true}"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "USER")
    void deleteResearchTask_ShouldForbidUser() throws Exception {
        mockMvc.perform(delete("/api/researchtasks/" + taskId))
                .andExpect(status().isForbidden());
    }
    @Test
    void allEndpoints_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/researchtasks"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/researchtasks"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/researchtasks/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/researchtasks/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(patch("/api/researchtasks/1/complete"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "USER")
    void getNonExistentResearchTask_ShouldReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        mockMvc.perform(get("/api/researchtasks/" + nonExistentId))
                .andExpect(status().isNotFound());
    }
}