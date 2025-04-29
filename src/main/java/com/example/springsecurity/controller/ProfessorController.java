package com.example.springsecurity.controller;

import com.example.springsecurity.dto.ProfessorDto;
import com.example.springsecurity.service.ProfessorsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private ProfessorsService professorsService;

    @PostMapping
    public ResponseEntity<ProfessorDto> createProfessor(@RequestBody ProfessorDto professorDto){
        ProfessorDto savedProfessor = professorsService.createProfessor(professorDto);
        return new ResponseEntity<>(savedProfessor, HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    public ResponseEntity<ProfessorDto> getProfessorById(@PathVariable("id") Long professorId) {
        ProfessorDto professorDto = professorsService.getProfessorById(professorId);
        return ResponseEntity.ok(professorDto);
    }
    @GetMapping
    public ResponseEntity<List<ProfessorDto>> getAllProfessors() {
        List<ProfessorDto> professors = professorsService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }
    @PutMapping("{id}")
    public ResponseEntity<ProfessorDto> updateProfessor(@PathVariable("id") Long professorId,
                                                      @RequestBody ProfessorDto updatedProfessor){
        ProfessorDto professorDto = professorsService.updateProfessor(professorId,updatedProfessor);
        return ResponseEntity.ok(professorDto);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProfessor(@PathVariable("id") Long professorId){
        professorsService.deleteProfessor(professorId);
        return ResponseEntity.ok("Professor deleted successfully!");
    }
}
