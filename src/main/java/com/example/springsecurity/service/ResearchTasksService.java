package com.example.springsecurity.service;

import com.example.springsecurity.dto.ResearchTasksDto;
import java.util.List;

public interface ResearchTasksService {

    ResearchTasksDto addResearchTask(ResearchTasksDto researchTasksDto);
    ResearchTasksDto getResearchTask(Long id);
    List<ResearchTasksDto> getAllResearchTasks();
    ResearchTasksDto updateResearchTask(ResearchTasksDto researchTasksDto,Long id);
    void deleteResearchTask(Long id);
    ResearchTasksDto completeResearchTask(Long id);
    ResearchTasksDto inCompleteResearchTask(Long id);

}
