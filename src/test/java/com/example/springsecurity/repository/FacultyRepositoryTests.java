package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Faculty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class FacultyRepositoryTests {

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void shouldSaveFaculty() {
        //arrange
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Computer Science");
        faculty.setFacultyDescription("CS Department");
        //act
        Faculty savedFaculty = facultyRepository.save(faculty);
        //assert
        assertThat(savedFaculty).isNotNull();
        assertThat(savedFaculty.getId()).isPositive();
        assertThat(savedFaculty.getFacultyName()).isEqualTo("Computer Science");
    }
    @Test
    void shouldFindFacultyById() {
        //arrange
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Mathematics");
        faculty.setFacultyDescription("Math Department");
        Faculty savedFaculty = facultyRepository.save(faculty);
        //act
        Optional<Faculty> foundFaculty = facultyRepository.findById(savedFaculty.getId());
        //assert
        assertThat(foundFaculty).isPresent();
        assertThat(foundFaculty.get().getFacultyName()).isEqualTo("Mathematics");
    }
    @Test
    void shouldReturnEmptyWhenFacultyNotFound() {
        Optional<Faculty> foundFaculty = facultyRepository.findById(999L);
        assertThat(foundFaculty).isEmpty();
    }
    @Test
    void shouldFindAllFaculties() {
        //arrange
        Faculty faculty1 = new Faculty();
        faculty1.setFacultyName("Faculty 1");
        faculty1.setFacultyDescription("Desc 1");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setFacultyName("Faculty 2");
        faculty2.setFacultyDescription("Desc 2");
        facultyRepository.save(faculty2);
        //act
        List<Faculty> faculties = facultyRepository.findAll();
        //assert
        assertThat(faculties).hasSize(2);
    }
    @Test
    void shouldDeleteFaculty() {
        //arrange
        Faculty faculty = new Faculty();
        faculty.setFacultyName("To Delete");
        faculty.setFacultyDescription("Desc");
        Faculty savedFaculty = facultyRepository.save(faculty);
        //act
        facultyRepository.deleteById(savedFaculty.getId());
        //assert
        assertThat(facultyRepository.findById(savedFaculty.getId())).isEmpty();
    }
}