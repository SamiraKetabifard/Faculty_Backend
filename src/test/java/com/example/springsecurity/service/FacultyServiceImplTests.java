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
        FacultyDto facultyDto = new FacultyDto(0L, "New Faculty", "Description"); // Use 0L instead of null
        Faculty faculty = FacultyMapper.mapToFacultyEntity(facultyDto);
        Faculty savedFaculty = new Faculty(1L, "New Faculty", "Description"); // Assuming 3-arg constructor

        when(facultyRepository.save(any(Faculty.class))).thenReturn(savedFaculty);

        FacultyDto result = facultyService.createFaculty(facultyDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFacultyName()).isEqualTo("New Faculty");
        verify(facultyRepository).save(any(Faculty.class));
    }
    @Test
    void shouldGetFacultyById() {
        Long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "Existing Faculty", "Description"); // 3-arg constructor

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));

        FacultyDto result = facultyService.getFacultiesById(facultyId);

        assertThat(result.getId()).isEqualTo(facultyId);
        assertThat(result.getFacultyName()).isEqualTo("Existing Faculty");
    }
    @Test
    void shouldThrowExceptionWhenFacultyNotFound() {
        Long facultyId = 1L;

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facultyService.getFacultiesById(facultyId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Faculty not found");
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
        Faculty existingFaculty = new Faculty(facultyId, "Old Name", "Old Desc");
        FacultyDto updatedDto = new FacultyDto(facultyId, "New Name", "New Desc");
        Faculty updatedFaculty = new Faculty(facultyId, "New Name", "New Desc");

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(existingFaculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);
        //act
        FacultyDto result = facultyService.updateFaculty(facultyId, updatedDto);
        //assert
        assertThat(result.getFacultyName()).isEqualTo("New Name");
        assertThat(result.getFacultyDescription()).isEqualTo("New Desc");
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