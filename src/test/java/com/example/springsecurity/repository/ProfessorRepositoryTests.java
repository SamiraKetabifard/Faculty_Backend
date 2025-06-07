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
        Faculty faculty = new Faculty();
        faculty.setFacultyName("Science");
        faculty.setFacultyDescription("Sci Dept");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Professors professor = new Professors();
        professor.setFirstName("Samira");
        professor.setLastName("Reza Mari");
        professor.setEmail("samira.reza.mari@gmail.com");
        professor.setFaculty(savedFaculty);

        Professors savedProfessor = professorRepository.save(professor);

        assertThat(savedProfessor).isNotNull();
        assertThat(savedProfessor.getId()).isPositive();
        assertThat(savedProfessor.getFaculty().getId()).isEqualTo(savedFaculty.getId());
    }

    @Test
    void shouldFindProfessorById() {
        Professors professor = new Professors();
        professor.setFirstName("Samira");
        professor.setLastName("Reza Mari");
        professor.setEmail("samira.reza.mari@gmail.com");
        Professors savedProfessor = professorRepository.save(professor);

        Optional<Professors> foundProfessor = professorRepository.findById(savedProfessor.getId());

        assertThat(foundProfessor).isPresent();
        assertThat(foundProfessor.get().getFirstName()).isEqualTo("Samira");
    }

    @Test
    void shouldFindAllProfessors() {
        Professors professor1 = new Professors();
        professor1.setFirstName("Samira");
        professor1.setLastName("Reza Mari");
        professor1.setEmail("samira.reza.mari1@gmail.com");
        professorRepository.save(professor1);

        Professors professor2 = new Professors();
        professor2.setFirstName("Samira");
        professor2.setLastName("Reza Mari");
        professor2.setEmail("samira.reza.mari2@gmail.com");
        professorRepository.save(professor2);

        List<Professors> professors = professorRepository.findAll();

        assertThat(professors).hasSize(2);
    }

    @Test
    void shouldFindProfessorsByFacultyUsingExistingRelationships() {
        Faculty faculty1 = new Faculty();
        faculty1.setFacultyName("Faculty 1");
        faculty1.setFacultyDescription("Desc");
        Faculty savedFaculty1 = facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setFacultyName("Faculty 2");
        faculty2.setFacultyDescription("Desc");
        Faculty savedFaculty2 = facultyRepository.save(faculty2);

        Professors professor1 = new Professors();
        professor1.setFirstName("Samira");
        professor1.setLastName("Reza Mari");
        professor1.setEmail("samira.reza.mari1@gmail.com");
        professor1.setFaculty(savedFaculty1);
        professorRepository.save(professor1);

        Professors professor2 = new Professors();
        professor2.setFirstName("Samira");
        professor2.setLastName("Reza Mari");
        professor2.setEmail("samira.reza.mari2@gmail.com");
        professor2.setFaculty(savedFaculty1);
        professorRepository.save(professor2);

        Professors professor3 = new Professors();
        professor3.setFirstName("Samira");
        professor3.setLastName("Reza Mari");
        professor3.setEmail("samira.reza.mari3@gmail.com");
        professor3.setFaculty(savedFaculty2);
        professorRepository.save(professor3);

        // Get all professors and filter by faculty ID (using primitive long comparison)
        List<Professors> allProfessors = professorRepository.findAll();
        List<Professors> faculty1Professors = allProfessors.stream()
                .filter(p -> p.getFaculty().getId() == savedFaculty1.getId())  // Using == for primitive long
                .toList();

        assertThat(faculty1Professors).hasSize(2);
        assertThat(faculty1Professors)
                .extracting(Professors::getEmail)
                .containsExactlyInAnyOrder("samira.reza.mari1@gmail.com", "samira.reza.mari2@gmail.com");
    }

    @Test
    void shouldDeleteProfessor() {
        Professors professor = new Professors();
        professor.setFirstName("Samira");
        professor.setLastName("Reza Mari");
        professor.setEmail("samira.reza.mari.delete@gmail.com");
        Professors savedProfessor = professorRepository.save(professor);

        professorRepository.deleteById(savedProfessor.getId());

        assertThat(professorRepository.findById(savedProfessor.getId())).isEmpty();
    }
}