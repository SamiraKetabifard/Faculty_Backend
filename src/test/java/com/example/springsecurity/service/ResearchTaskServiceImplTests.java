package com.example.springsecurity.service;

import com.example.springsecurity.dto.ResearchTasksDto;
import com.example.springsecurity.entity.ResearchTasks;
import com.example.springsecurity.exception.ResourceNotFoundException;
import com.example.springsecurity.mapper.ResearchTaskMapper;
import com.example.springsecurity.repository.ResearchTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResearchTaskServiceImplTests {

    @Mock
    private ResearchTaskRepository researchTaskRepository;

    @InjectMocks
    private ResearchTaskServiceImpl researchTaskService;

    @Test
    void shouldCreateResearchTask() {
        //arrange
        ResearchTasksDto dto = new ResearchTasksDto(null, "New Task", "Description", false);
        ResearchTasks task = ResearchTaskMapper.mapToResearchTasksEntity(dto);
        ResearchTasks savedTask = new ResearchTasks(1L, "New Task", "Description", false);
        when(researchTaskRepository.save(any(ResearchTasks.class))).thenReturn(savedTask);
        //act
        ResearchTasksDto result = researchTaskService.addResearchTask(dto);
        //assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New Task");
        verify(researchTaskRepository).save(any(ResearchTasks.class));
    }
    @Test
    void shouldCompleteResearchTask() {
        //arrange
        Long taskId = 1L;
        ResearchTasks task = new ResearchTasks(taskId, "Task", "Desc", false);
        ResearchTasks completedTask = new ResearchTasks(taskId, "Task", "Desc", true);
        when(researchTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(researchTaskRepository.save(any(ResearchTasks.class))).thenReturn(completedTask);
        //act
        ResearchTasksDto result = researchTaskService.completeResearchTask(taskId);
        //assert
        assertThat(result.isCompleted()).isTrue();
        verify(researchTaskRepository).save(task);
    }
    @Test
    void shouldIncompleteResearchTask() {
        //arrange
        Long taskId = 1L;
        ResearchTasks task = new ResearchTasks(taskId, "Task", "Desc", true);
        ResearchTasks incompletedTask = new ResearchTasks(taskId, "Task", "Desc", false);
        when(researchTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(researchTaskRepository.save(any(ResearchTasks.class))).thenReturn(incompletedTask);
        //act
        ResearchTasksDto result = researchTaskService.inCompleteResearchTask(taskId);
        //assert
        assertThat(result.isCompleted()).isFalse();
        verify(researchTaskRepository).save(task);
    }
    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        //arrange
        Long taskId = 1L;
        //act
        when(researchTaskRepository.findById(taskId)).thenReturn(Optional.empty());
        //assert
        assertThatThrownBy(() -> researchTaskService.getResearchTask(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("ResearchTasks not found");
    }
    @Test
    void shouldDeleteResearchTask() {
        //arrange
        Long taskId = 1L;
        ResearchTasks task = new ResearchTasks(taskId, "To Delete", "Desc", false);
        when(researchTaskRepository.findById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(researchTaskRepository).deleteById(taskId);
        //act
        researchTaskService.deleteResearchTask(taskId);
        //assert
        verify(researchTaskRepository).deleteById(taskId);
    }
}