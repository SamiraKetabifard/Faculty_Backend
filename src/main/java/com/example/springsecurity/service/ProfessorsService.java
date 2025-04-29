package com.example.springsecurity.service;

import com.example.springsecurity.dto.ProfessorDto;
import java.util.List;

public interface ProfessorsService {

    ProfessorDto createProfessor(ProfessorDto professorDto);
    ProfessorDto getProfessorById(Long professorId);
    List<ProfessorDto> getAllProfessors();
    ProfessorDto updateProfessor(Long professorId,ProfessorDto updatedProfessor);
    void deleteProfessor(Long professorId);

}
