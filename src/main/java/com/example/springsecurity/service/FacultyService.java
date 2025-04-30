package com.example.springsecurity.service;

import com.example.springsecurity.dto.FacultyDto;
import java.util.List;

public interface FacultyService {

    FacultyDto createFaculty(FacultyDto facultyDto);
    FacultyDto getFacultiesById(Long facultyId);
    List<FacultyDto> getAllFaculties();
    FacultyDto updateFaculty(Long facultyId, FacultyDto facultyDto);
    void deleteFacultyById(Long id);

}

