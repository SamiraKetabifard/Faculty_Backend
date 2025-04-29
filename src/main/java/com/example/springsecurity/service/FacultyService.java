package com.example.springsecurity.service;

import com.example.springsecurity.dto.FacultyDto;
import java.util.List;

public interface FacultyService {

    FacultyDto createFaculty(FacultyDto facultyDto);
    FacultyDto getFacultiesById(long facultyId);
    List<FacultyDto> getAllFaculties();
    FacultyDto updateFaculty(long facultyId, FacultyDto facultyDto);
    void deleteFacultyById(long id);

}

