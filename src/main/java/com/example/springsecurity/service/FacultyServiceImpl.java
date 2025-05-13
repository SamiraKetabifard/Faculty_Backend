package com.example.springsecurity.service;

import com.example.springsecurity.dto.FacultyDto;
import com.example.springsecurity.entity.Faculty;
import com.example.springsecurity.exception.ResourceNotFoundException;
import com.example.springsecurity.mapper.FacultyMapper;
import com.example.springsecurity.repository.FacultyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private FacultyRepository facultyRepository;

    @Override
    public FacultyDto createFaculty(FacultyDto facultyDto) {
        Faculty faculty = FacultyMapper.mapToFacultyEntity(facultyDto);
        Faculty savedFaculty = facultyRepository.save(faculty);
        return FacultyMapper.mapToFacultyDto(savedFaculty);
    }
    @Override
    public FacultyDto getFacultiesById(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId).
                orElseThrow(()-> new ResourceNotFoundException("Faculty not found"));
        return FacultyMapper.mapToFacultyDto(faculty);
    }
    @Override
    public List<FacultyDto> getAllFaculties() {
        List<Faculty> facultyList = facultyRepository.findAll();
        return facultyList.stream().map(FacultyMapper::mapToFacultyDto).collect(Collectors.toList());
        /*  return facultyList.stream()
                .map(faculty -> FacultyMapper.mapToFacultyDto(faculty))
               .collect(Collectors.toList());*/
    }
    @Override
    public FacultyDto updateFaculty(Long facultyId, FacultyDto facultyDto) {
        Faculty faculty = facultyRepository.findById(facultyId).
                orElseThrow(()-> new ResourceNotFoundException("Faculty not found"));
        faculty.setFacultyName(facultyDto.getFacultyName());
        faculty.setFacultyDescription(facultyDto.getFacultyDescription());
        facultyRepository.save(faculty);
        return FacultyMapper.mapToFacultyDto(faculty);
    }
    @Override
    public void deleteFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Faculty not found"));
        facultyRepository.deleteById(faculty.getId());
    }
}
