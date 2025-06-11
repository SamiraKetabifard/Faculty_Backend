package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Faculty;
import com.example.springsecurity.entity.Professors;
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
class ProfessorRepositoryTests {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void shouldSaveProfessorWithFaculty() {
        //arrange
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Science");
        faculty.setFacultyDescription("Sci Dept");
        Faculty savedFaculty = facultyRepository.save(faculty);
        Professors professor = new Professors();
        professor.setFirstName("samira");
        professor.setLastName("ketabi");
        professor.setEmail("samira@gmail.com");
        professor.setFaculty(savedFaculty);
        //act
        Professors savedProfessor = professorRepository.save(professor);
        //assert
        assertThat(savedProfessor).isNotNull();
        assertThat(savedProfessor.getId()).isPositive();
        assertThat(savedProfessor.getFaculty().getId()).isEqualTo(savedFaculty.getId());
    }
    @Test
    void shouldFindProfessorById() {
        //arrange
        Professors professor = new Professors();
        professor.setFirstName("samira");
        professor.setLastName("ketabi");
        professor.setEmail("samira@gmail.com");
        Professors savedProfessor = professorRepository.save(professor);
        //act
        Optional<Professors> foundProfessor = professorRepository.findById(savedProfessor.getId());
        //assert
        assertThat(foundProfessor).isPresent();
        assertThat(foundProfessor.get().getFirstName()).isEqualTo("samira");
    }
    @Test
    void shouldFindAllProfessors() {
        //arrange
        Professors professor1 = new Professors();
        professor1.setFirstName("samira");
        professor1.setLastName("ketabi");
        professor1.setEmail("samira@gmail.com");
        professorRepository.save(professor1);

        Professors professor2 = new Professors();
        professor2.setFirstName("mari");
        professor2.setLastName("ketabi");
        professor2.setEmail("mari@gmail.com");
        professorRepository.save(professor2);
        //act
        List<Professors> professors = professorRepository.findAll();
        //assert
        assertThat(professors).hasSize(2);
    }
    @Test
    void shouldFindProfessorsByFacultyUsingExistingRelationships() {
        //arrange
        Faculty faculty1 = new Faculty();
        faculty1.setFacultyName("Faculty 1");
        faculty1.setFacultyDescription("Desc");
        Faculty savedFaculty1 = facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setFacultyName("Faculty 2");
        faculty2.setFacultyDescription("Desc");
        Faculty savedFaculty2 = facultyRepository.save(faculty2);

        Professors professor1 = new Professors();
        professor1.setFirstName("samira");
        professor1.setLastName("ketabi");
        professor1.setEmail("samira@gmail.com");
        professor1.setFaculty(savedFaculty1);
        professorRepository.save(professor1);

        Professors professor2 = new Professors();
        professor2.setFirstName("mari");
        professor2.setLastName("ketabi");
        professor2.setEmail("mari@gmail.com");
        professor2.setFaculty(savedFaculty1);
        professorRepository.save(professor2);

        Professors professor3 = new Professors();
        professor3.setFirstName("joe");
        professor3.setLastName("anderson");
        professor3.setEmail("j@gmail.com");
        professor3.setFaculty(savedFaculty2);
        professorRepository.save(professor3);
        //act
        // Get all professors and filter by faculty ID (using primitive long comparison)
        List<Professors> allProfessors = professorRepository.findAll();
        List<Professors> faculty1Professors = allProfessors.stream()
                .filter(p -> p.getFaculty().getId() == savedFaculty1.getId())  // Using == for primitive long
                .toList();
        //assert
        assertThat(faculty1Professors).hasSize(2);
        assertThat(faculty1Professors)
                .extracting(Professors::getEmail)
                .containsExactlyInAnyOrder("samira@gmail.com", "mari@gmail.com");
    }
    @Test
    void shouldDeleteProfessor() {
        //arrange
        Professors professor = new Professors();
        professor.setFirstName("Samira");
        professor.setLastName("ketabi");
        professor.setEmail("samira@gmail.com");
        Professors savedProfessor = professorRepository.save(professor);
        //act
        professorRepository.deleteById(savedProfessor.getId());
        //assert
        assertThat(professorRepository.findById(savedProfessor.getId())).isEmpty();
    }
}