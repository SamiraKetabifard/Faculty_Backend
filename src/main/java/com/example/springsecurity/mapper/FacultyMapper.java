package com.example.springsecurity.mapper;

import com.example.springsecurity.dto.FacultyDto;
import com.example.springsecurity.entity.Faculty;

//manual mapper
public class FacultyMapper {

    //Entity to DTO
    public static FacultyDto mapToFacultyDto(Faculty faculty) {
        return new FacultyDto(
                faculty.getId(),
                faculty.getFacultyName(),
                faculty.getFacultyDescription());
    }
    //DTO to Entity
    public static Faculty mapToFacultyEntity(FacultyDto facultyDto) {
        return new Faculty(
                facultyDto.getId(),
                facultyDto.getFacultyName(),
                facultyDto.getFacultyDescription());
    }
}
