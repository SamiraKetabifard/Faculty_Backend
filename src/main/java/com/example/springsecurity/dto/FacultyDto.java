package com.example.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacultyDto {
    private long id;
    private String facultyName;
    private String facultyDescription;
}
