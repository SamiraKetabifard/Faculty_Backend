package com.example.springsecurity.service;

import com.example.springsecurity.dto.ProfessorDto;
import com.example.springsecurity.entity.Faculty;
import com.example.springsecurity.entity.Professors;
import com.example.springsecurity.exception.ResourceNotFoundException;
import com.example.springsecurity.mapper.ProfessorMapper;
import com.example.springsecurity.repository.FacultyRepository;
import com.example.springsecurity.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceImplTests {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @Test
    void shouldCreateProfessor() {
        Long facultyId = 1L;
        ProfessorDto professorDto = new ProfessorDto(0L, "Samira", "Reza Roz", "samira.reza.roz@example.com", facultyId);

        // Assuming Faculty has a 3-arg constructor (id, name, description)
        Faculty faculty = new Faculty(facultyId, "Science", "Sci Dept");

        Professors professor = ProfessorMapper.mapToProfessors(professorDto);
        professor.setFaculty(faculty);
        Professors savedProfessor = new Professors(1L, "Samira", "Reza Roz", "samira.reza.roz@example.com", faculty);

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(professorRepository.save(any(Professors.class))).thenReturn(savedProfessor);

        ProfessorDto result = professorService.createProfessor(professorDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("samira.reza.roz@example.com");
        verify(professorRepository).save(any(Professors.class));
    }

    @Test
    void shouldThrowExceptionWhenFacultyNotFound() {
        Long facultyId = 1L;
        ProfessorDto professorDto = new ProfessorDto(0L, "Samira", "Reza Roz", "samira.reza.roz@example.com", facultyId);

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.createProfessor(professorDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("faculty id is not found");
    }

    @Test
    void shouldGetProfessorById() {
        Long professorId = 1L;
        Faculty faculty = new Faculty(1L, "Science", "Sci Dept");
        Professors professor = new Professors(professorId, "Samira", "Reza Roz", "samira.reza.roz@example.com", faculty);

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));

        ProfessorDto result = professorService.getProfessorById(professorId);

        assertThat(result.getId()).isEqualTo(professorId);
        assertThat(result.getFirstName()).isEqualTo("Samira");
        assertThat(result.getFacultyId()).isEqualTo(1L);
    }

    @Test
    void shouldUpdateProfessor() {
        Long professorId = 1L;
        Long facultyId = 1L;
        Long newFacultyId = 2L;

        Faculty oldFaculty = new Faculty(facultyId, "Old Faculty", "Desc");
        Faculty newFaculty = new Faculty(newFacultyId, "New Faculty", "Desc");

        Professors existingProfessor = new Professors(professorId, "Samira", "Reza Roz", "samira.reza.roz.old@example.com", oldFaculty);
        ProfessorDto updatedDto = new ProfessorDto(professorId, "Samira", "Reza Roz", "samira.reza.roz.new@example.com", newFacultyId);
        Professors updatedProfessor = new Professors(professorId, "Samira", "Reza Roz", "samira.reza.roz.new@example.com", newFaculty);

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(existingProfessor));
        when(facultyRepository.findById(newFacultyId)).thenReturn(Optional.of(newFaculty));
        when(professorRepository.save(any(Professors.class))).thenReturn(updatedProfessor);

        ProfessorDto result = professorService.updateProfessor(professorId, updatedDto);

        assertThat(result.getFirstName()).isEqualTo("Samira");
        assertThat(result.getEmail()).isEqualTo("samira.reza.roz.new@example.com");
        assertThat(result.getFacultyId()).isEqualTo(newFacultyId);
        verify(professorRepository).save(existingProfessor);
    }
}