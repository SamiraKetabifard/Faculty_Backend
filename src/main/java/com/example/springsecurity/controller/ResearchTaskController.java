package com.example.springsecurity.controller;

import com.example.springsecurity.dto.ResearchTasksDto;
import com.example.springsecurity.service.ResearchTasksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/researchtasks")
public class ResearchTaskController {
    private ResearchTasksService researchTasksService;

    public ResearchTaskController(ResearchTasksService researchTasksService) {
        this.researchTasksService = researchTasksService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResearchTasksDto> createResearchTask(@RequestBody ResearchTasksDto researchTasksDto) {
        return new ResponseEntity<>(researchTasksService.addResearchTask(researchTasksDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("{id}")
    public ResponseEntity<ResearchTasksDto> getResearchTasksById(@PathVariable("id") long id) {
        return new ResponseEntity<>(researchTasksService.getResearchTask(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ResearchTasksDto>> getAllResearchTasks() {
        return new ResponseEntity<>(researchTasksService.getAllResearchTasks(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity <ResearchTasksDto> updateResearchTask(@PathVariable("id") long id,
                                                                @RequestBody ResearchTasksDto researchTaskDto) {
        return new ResponseEntity<>(researchTasksService.updateResearchTask(researchTaskDto,id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String>deleteResearchTaskById(@PathVariable ("id")long id){
        researchTasksService.deleteResearchTask(id);
        return new ResponseEntity<>("deleted id:"+id,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("{id}/complete")
    public ResponseEntity<ResearchTasksDto> CompleteResearchTask(@PathVariable("id")Long researchTaskId) {
        ResearchTasksDto updatedResearchTask= researchTasksService.completeResearchTask(researchTaskId);
        return ResponseEntity.ok(updatedResearchTask);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("{id}/in-complete")
    public ResponseEntity<ResearchTasksDto> inCompleteResearchTask(@PathVariable("id")Long researchTaskId) {
        ResearchTasksDto updatedResearchTask= researchTasksService.inCompleteResearchTask(researchTaskId);
        return ResponseEntity.ok(updatedResearchTask);
    }
}