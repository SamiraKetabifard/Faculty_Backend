package com.example.springsecurity.service;

import com.example.springsecurity.dto.ResearchTasksDto;
import com.example.springsecurity.entity.ResearchTasks;
import com.example.springsecurity.exception.ResourceNotFoundException;
import com.example.springsecurity.mapper.ResearchTaskMapper;
import com.example.springsecurity.repository.ResearchTaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResearchTaskServiceImpl implements ResearchTasksService{

    private final ResearchTaskRepository researchTaskRepository;

    public ResearchTaskServiceImpl(ResearchTaskRepository researchTaskRepository) {
        this.researchTaskRepository = researchTaskRepository;
    }
    @Override
    public ResearchTasksDto addResearchTask(ResearchTasksDto researchTasksDto) {
        ResearchTasks researchTasks = ResearchTaskMapper.mapToResearchTasksEntity(researchTasksDto);
        ResearchTasks savedResearchTasks = researchTaskRepository.save(researchTasks);
        return ResearchTaskMapper.mapToResearchTaskDto(savedResearchTasks);
    }
    @Override
    public ResearchTasksDto getResearchTask(Long id) {
        ResearchTasks researchTasks = researchTaskRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("ResearchTasks not found"));
        return ResearchTaskMapper.mapToResearchTaskDto(researchTasks);
    }
    @Override
    public List<ResearchTasksDto> getAllResearchTasks() {
        List<ResearchTasks> researchTasks = researchTaskRepository.findAll();
        return researchTasks.stream().map(ResearchTaskMapper::mapToResearchTaskDto).collect(Collectors.toList());
    }
    @Override
    public ResearchTasksDto updateResearchTask(ResearchTasksDto researchTasksDto, Long id) {
        ResearchTasks researchTasks = researchTaskRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("ResearchTasks not found"));
        researchTasks.setTitle(researchTasksDto.getTitle());
        researchTasks.setDescription(researchTasksDto.getDescription());
        researchTasks.setCompleted(researchTasksDto.isCompleted());
        return ResearchTaskMapper.mapToResearchTaskDto(researchTaskRepository.save(researchTasks));
    }
    @Override
    public void deleteResearchTask(Long id) {
        ResearchTasks researchTasks = researchTaskRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("ResearchTasks not found"));
        researchTaskRepository.deleteById(researchTasks.getId());
    }
    @Override
    public ResearchTasksDto completeResearchTask(Long id) {
        ResearchTasks researchTasks = researchTaskRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("ResearchTasks not found"));
        researchTasks.setCompleted(Boolean.TRUE);
        ResearchTasks updatedResearchTasks = researchTaskRepository.save(researchTasks);
        return ResearchTaskMapper.mapToResearchTaskDto(updatedResearchTasks);
    }
    @Override
    public ResearchTasksDto inCompleteResearchTask(Long id) {
        ResearchTasks researchTasks = researchTaskRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("ResearchTasks not found"));
        researchTasks.setCompleted(Boolean.FALSE);
        ResearchTasks updatedResearchTasks = researchTaskRepository.save(researchTasks);
        return ResearchTaskMapper.mapToResearchTaskDto(updatedResearchTasks);
    }
}
