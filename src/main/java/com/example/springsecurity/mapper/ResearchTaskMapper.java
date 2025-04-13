package com.example.springsecurity.mapper;

import com.example.springsecurity.dto.ResearchTasksDto;
import com.example.springsecurity.entity.ResearchTasks;

public class ResearchTaskMapper {

    public static ResearchTasksDto mapToResearchTaskDto(ResearchTasks researchTasks) {
        return new ResearchTasksDto(
                researchTasks.getId(),
                researchTasks.getTitle(),
                researchTasks.getDescription(),
                researchTasks.isCompleted()
        );
    }
    public  static ResearchTasks mapToResearchTasksEntity(ResearchTasksDto researchTasksDto) {
        return new ResearchTasks(
                researchTasksDto.getId(),
                researchTasksDto.getTitle(),
                researchTasksDto.getDescription(),
                researchTasksDto.isCompleted()
        );
    }
}
