package com.example.springsecurity.controller;

import com.example.springsecurity.dto.FacultyDto;
import com.example.springsecurity.service.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/faculties")
public class FacultyController {

    FacultyService facultyService;
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @PostMapping
    public ResponseEntity<FacultyDto> createFaculty(@RequestBody FacultyDto facultyDto) {
        FacultyDto savedFaculty=facultyService.createFaculty(facultyDto);
        return new ResponseEntity<>(savedFaculty,HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    public ResponseEntity<FacultyDto> getFacultyById(@PathVariable("id") long id) {
        return new ResponseEntity<>(facultyService.getFacultiesById(id),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<FacultyDto>>getAllFaculties(){
        return new ResponseEntity<>(facultyService.getAllFaculties(),HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<FacultyDto> updateFaculty(@PathVariable("id") long id,
                                                    @RequestBody FacultyDto facultyDto) {
        return new ResponseEntity<>(facultyService.updateFaculty(id, facultyDto),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteFaculty(@PathVariable("id") long id) {
        facultyService.deleteFacultyById(id);
        return new ResponseEntity<>("deleted id:"+id,HttpStatus.OK);
    }
}
