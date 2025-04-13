package com.example.springsecurity.mapper;

import com.example.springsecurity.dto.ProfessorDto;
import com.example.springsecurity.entity.Professors;

public class ProfessorMapper {

    public static ProfessorDto mapToProfessorDto(Professors professors) {
        return new ProfessorDto(
                professors.getId(),
                professors.getFirstName(),
                professors.getLastName(),
                professors.getEmail(),
                professors.getFaculty().getId()
        );
    }

    public static Professors mapToProfessors(ProfessorDto professorDto) {
        Professors professors = new Professors();
        professors.setId(professorDto.getId());
        professors.setFirstName(professorDto.getFirstName());
        professors.setLastName(professorDto.getLastName());
        professors.setEmail(professorDto.getEmail());
        return professors;
    }
}
