package com.example.springsecurity.controller;

import com.example.springsecurity.entity.Faculty;
import com.example.springsecurity.repository.FacultyRepository;
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
class FacultyControllerSecurityTests{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FacultyRepository facultyRepository;

    private Long facultyId;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Test Faculty");
        faculty.setFacultyDescription("Test Description");
        facultyId = facultyRepository.save(faculty).getId();
    }
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getAllFaculties_ShouldAllowAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/faculties"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getFacultyById_ShouldAllowAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/faculties/" + facultyId))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void createFaculty_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(post("/api/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"facultyName\":\"New Faculty\",\"facultyDescription\":\"New Desc\"}"))
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateFaculty_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(put("/api/faculties/" + facultyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"facultyName\":\"Updated Faculty\",\"facultyDescription\":\"Updated Desc\"}"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFaculty_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(delete("/api/faculties/" + facultyId))
                .andExpect(status().isOk());
    }
    @Test
    void allEndpoints_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/faculties"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/faculties"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/faculties/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/faculties/1"))
                .andExpect(status().isUnauthorized());
    }
}
