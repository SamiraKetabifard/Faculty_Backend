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