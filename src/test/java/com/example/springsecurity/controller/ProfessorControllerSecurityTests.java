package com.example.springsecurity.controller;

import com.example.springsecurity.entity.Faculty;
import com.example.springsecurity.entity.Professors;
import com.example.springsecurity.repository.FacultyRepository;
import com.example.springsecurity.repository.ProfessorRepository;
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
class ProfessorControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Long professorId;
    private Long facultyId;

    @BeforeEach
    void setUp() {
        professorRepository.deleteAll();
        facultyRepository.deleteAll();

        Faculty faculty = new Faculty();
        faculty.setFacultyName("Test Faculty");
        faculty.setFacultyDescription("Test Description");
        facultyId = facultyRepository.save(faculty).getId();

        Professors professor = new Professors();
        professor.setFirstName("samira");
        professor.setLastName("ketabi");
        professor.setEmail("samira@gmail.com");
        professor.setFaculty(faculty);
        professorId = professorRepository.save(professor).getId();
    }
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getAllProfessors_ShouldAllowAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/professors"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void getProfessorById_ShouldAllowAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/professors/" + professorId))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void createProfessor_ShouldAllowAdmin() throws Exception {
        String professorJson = String.format(
                "{\"firstName\":\"Mari\",\"lastName\":\"rezaei\",\"email\":\"mari@gmail.com\",\"facultyId\":%d}",
                facultyId);
        mockMvc.perform(post("/api/professors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson))
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProfessor_ShouldAllowAdmin() throws Exception {
        String professorJson = String.format(
                "{\"firstName\":\"samira\",\"lastName\":\"ketabi\",\"email\":\"samira@gmail.com\",\"facultyId\":%d}",
                facultyId);
        mockMvc.perform(put("/api/professors/" + professorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(professorJson))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProfessor_ShouldAllowAdmin() throws Exception {
        mockMvc.perform(delete("/api/professors/" + professorId))
                .andExpect(status().isOk());
    }
    @Test
    void allEndpoints_ShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/professors"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/professors"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put("/api/professors/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/api/professors/1"))
                .andExpect(status().isUnauthorized());
    }
}