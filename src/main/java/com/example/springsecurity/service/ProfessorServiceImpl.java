package com.example.springsecurity.service;

import com.example.springsecurity.dto.ProfessorDto;
import com.example.springsecurity.entity.Faculty;
import com.example.springsecurity.entity.Professors;
import com.example.springsecurity.exception.ResourceNotFoundException;
import com.example.springsecurity.mapper.ProfessorMapper;
import com.example.springsecurity.repository.FacultyRepository;
import com.example.springsecurity.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImpl implements ProfessorsService{

    private final ProfessorRepository professorRepository;
    private final FacultyRepository facultyRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository,
                                FacultyRepository facultyRepository) {
        this.professorRepository = professorRepository;
        this.facultyRepository = facultyRepository;
    }
    @Override
    public ProfessorDto createProfessor(ProfessorDto professorDto){
        Faculty faculty = facultyRepository.findById(professorDto.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("faculty id is not found"));
        Professors professors = ProfessorMapper.mapToProfessors(professorDto);
        professors.setFaculty(faculty);
        Professors savedProfessor = professorRepository.save(professors);
        return ProfessorMapper.mapToProfessorDto(savedProfessor);
    }
    @Override
    public ProfessorDto getProfessorById(Long professorId){
        Professors professors = professorRepository.findById(professorId).
                orElseThrow(()-> new ResourceNotFoundException("Professor does not exist with given id"));
        return ProfessorMapper.mapToProfessorDto(professors);
    }
    @Override
    public List<ProfessorDto> getAllProfessors(){
        List<Professors> professor = professorRepository.findAll();
        return professor.stream().map((professors)->
                        ProfessorMapper. mapToProfessorDto(professors)).
                collect(Collectors.toList());
    }
    @Override
    public ProfessorDto updateProfessor(Long professorId, ProfessorDto updatedProfessor){
        Professors professors = professorRepository.findById(professorId).
                orElseThrow(() -> new ResourceNotFoundException("Professor does not exist with given id"));
        Faculty faculty = facultyRepository.findById(updatedProfessor.getFacultyId()).
                orElseThrow(() -> new ResourceNotFoundException("Faculty does not exist with given id"));
        professors.setFirstName(updatedProfessor.getFirstName());
        professors.setLastName(updatedProfessor.getLastName());
        professors.setEmail(updatedProfessor.getEmail());
        professors.setFaculty(faculty);
        Professors updatedProfessorObj = professorRepository.save(professors);
        return ProfessorMapper.mapToProfessorDto(updatedProfessorObj);
    }
    @Override
    public void deleteProfessor(Long professorId) {
        Professors professors = professorRepository.findById(professorId).
                orElseThrow(() -> new ResourceNotFoundException("Professor does not exist with given id"));
        professorRepository.deleteById(professors.getId());
    }
}
