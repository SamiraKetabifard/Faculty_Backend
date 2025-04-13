package com.example.springsecurity.repository;

import com.example.springsecurity.entity.ResearchTasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResearchTaskRepository extends JpaRepository<ResearchTasks,Long> {
}
