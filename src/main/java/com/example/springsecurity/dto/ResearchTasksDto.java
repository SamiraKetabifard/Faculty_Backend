package com.example.springsecurity.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResearchTasksDto {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
