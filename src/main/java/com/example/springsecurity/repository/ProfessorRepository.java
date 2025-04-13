package com.example.springsecurity.repository;

import com.example.springsecurity.entity.Professors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professors,Long> {
}
