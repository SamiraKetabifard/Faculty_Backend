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
        //arrange
        Long facultyId = 1L;
        ProfessorDto professorDto = new ProfessorDto(0L, "Samira",
                "Ketabi", "s@gmail.com", facultyId);
        Faculty faculty = new Faculty(facultyId, "Science", "Sci Dept");
        Professors professor = ProfessorMapper.mapToProfessors(professorDto);
        professor.setFaculty(faculty);
        Professors savedProfessor = new Professors(1L, "Samira", "Ketabi",
                "s@gmail.com", faculty);
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(professorRepository.save(any(Professors.class))).thenReturn(savedProfessor);
        //act
        ProfessorDto result = professorService.createProfessor(professorDto);
        //assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("s@gmail.com");
        verify(professorRepository).save(any(Professors.class));
    }
    @Test
    void shouldThrowExceptionWhenFacultyNotFound() {
        //arrange
        Long facultyId = 1000L;
        ProfessorDto professorDto = new ProfessorDto(0L, "Samira", "Ketabi",
                "samira@gmail.com", facultyId);
        //act
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());
        //assert
        assertThatThrownBy(() -> professorService.createProfessor(professorDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("faculty id is not found");
    }
    @Test
    void shouldGetProfessorById() {
        //arrange
        Long professorId = 1L;
        Faculty faculty = new Faculty(1L, "Science", "Sci Dept");
        Professors professor = new Professors(professorId, "Samira", "Ketabi",
                "samira@gmail.com", faculty);
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        //act
        ProfessorDto result = professorService.getProfessorById(professorId);
        //assert
        assertThat(result.getId()).isEqualTo(professorId);
        assertThat(result.getFirstName()).isEqualTo("Samira");
        assertThat(result.getFacultyId()).isEqualTo(1L);
    }
    @Test
    void shouldUpdateProfessor() {
        // Arrange
        Long professorId = 1L;
        Long oldFacultyId = 1L;
        Long newFacultyId = 2L;
        Faculty oldFaculty = new Faculty(oldFacultyId, "Old Faculty", "Desc");
        Faculty newFaculty = new Faculty(newFacultyId, "New Faculty", "Desc");
        Professors existingProfessor = new Professors(
                professorId, "Samira", "Ketabi", "samira@gmail.com", oldFaculty);
        ProfessorDto updatedDto = new ProfessorDto(
                professorId, "Samira", "Ketabifard", "samira1@gmail.com", newFacultyId);
        Professors updatedProfessor = new Professors(
                professorId, "Samira", "Ketabifard", "samira1@gmail.com", newFaculty);
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(existingProfessor));
        when(facultyRepository.findById(newFacultyId)).thenReturn(Optional.of(newFaculty));
        when(professorRepository.save(any(Professors.class))).thenReturn(updatedProfessor);
        // Act
        ProfessorDto result = professorService.updateProfessor(professorId, updatedDto);
        // Assert
        assertThat(result.getFirstName()).isEqualTo("Samira");
        assertThat(result.getLastName()).isEqualTo("Ketabifard");
        assertThat(result.getEmail()).isEqualTo("samira1@gmail.com");
        assertThat(result.getFacultyId()).isEqualTo(newFacultyId);
        verify(professorRepository).findById(professorId);
        verify(facultyRepository).findById(newFacultyId);
        verify(professorRepository).save(existingProfessor);
    }
}