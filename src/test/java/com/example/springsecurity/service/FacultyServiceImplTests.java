package com.example.springsecurity.service;

import com.example.springsecurity.dto.FacultyDto;
import com.example.springsecurity.entity.Faculty;
import com.example.springsecurity.exception.ResourceNotFoundException;
import com.example.springsecurity.mapper.FacultyMapper;
import com.example.springsecurity.repository.FacultyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceImplTests {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    @Test
    void shouldCreateFaculty() {
        //0L instead of null
        //arrange
        FacultyDto facultyDto = new FacultyDto(0L, "New Faculty", "Description");
        Faculty faculty = FacultyMapper.mapToFacultyEntity(facultyDto);
        Faculty savedFaculty = FacultyMapper.mapToFacultyEntity(facultyDto);
        savedFaculty.setId(1L);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(savedFaculty);
        //act
        FacultyDto result = facultyService.createFaculty(facultyDto);
        //assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFacultyName()).isEqualTo("New Faculty");
        verify(facultyRepository).save(any(Faculty.class));
    }
    @Test
    void shouldGetFacultyById() {
        //arrange
        Long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "Existing Faculty", "Description");
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        //act
        FacultyDto result = facultyService.getFacultiesById(facultyId);
        //assert
        assertThat(result.getId()).isEqualTo(facultyId);
        assertThat(result.getFacultyName()).isEqualTo("Existing Faculty");
    }
    @Test
    void shouldThrowExceptionWhenFacultyNotFound() {
        //arrange
        Long facultyId = 1L;
        //act
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());
        //assert
        assertThatThrownBy(() -> facultyService.getFacultiesById(facultyId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Faculty not found");
        //ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
        //        () -> facultyService.getFacultiesById(facultyId));
        //assertEquals("Faculty not found", ex.getMessage());
    }
    @Test
    void shouldGetAllFaculties() {
        //arrange
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Faculty 1", "Desc 1"),
                new Faculty(2L, "Faculty 2", "Desc 2"));
        when(facultyRepository.findAll()).thenReturn(faculties);
        //act
        List<FacultyDto> result = facultyService.getAllFaculties();
        //assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFacultyName()).isEqualTo("Faculty 1");
        assertThat(result.get(1).getFacultyName()).isEqualTo("Faculty 2");
    }
    @Test
    void shouldUpdateFaculty() {
        //arrange
        Long facultyId = 1L;
        Faculty existingFaculty = new Faculty(facultyId, "ai1", "ai1");
        FacultyDto updatedDto = new FacultyDto(facultyId, "ai1", "ai1");
        Faculty updatedFaculty = new Faculty(facultyId, "ai2", "ai2");
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(existingFaculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);
        //act
        FacultyDto result = facultyService.updateFaculty(facultyId, updatedDto);
        //assert
        assertThat(result.getFacultyName()).isEqualTo("ai1");
        assertThat(result.getFacultyDescription()).isEqualTo("ai1");
        verify(facultyRepository).save(existingFaculty);
    }
    @Test
    void shouldDeleteFaculty() {
        //arrange
        Long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "To Delete", "Desc");
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        doNothing().when(facultyRepository).deleteById(facultyId);
        //act
        facultyService.deleteFacultyById(facultyId);
        //assert
        verify(facultyRepository).deleteById(facultyId);
    }
}