package com.example.springsecurity.repository;

import com.example.springsecurity.entity.ResearchTasks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ResearchTaskRepositoryTests {

    @Autowired
    private ResearchTaskRepository researchTaskRepository;

    @Test
    void shouldSaveResearchTask() {
        // Arrange
        ResearchTasks task = new ResearchTasks();
        task.setTitle("New Research");
        task.setDescription("Research Description");
        task.setCompleted(false);
        // Act
        ResearchTasks savedTask = researchTaskRepository.save(task);
        // Assert
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isPositive();
        assertThat(savedTask.getTitle()).isEqualTo("New Research");
        assertThat(savedTask.getDescription()).isEqualTo("Research Description");
        assertThat(savedTask.isCompleted()).isFalse();
    }
    @Test
    void shouldFindTaskById() {
        // Arrange
        ResearchTasks task = new ResearchTasks();
        task.setTitle("task1");
        task.setDescription("Task to find");
        task.setCompleted(true);
        ResearchTasks savedTask = researchTaskRepository.save(task);
        // Act
        Optional<ResearchTasks> foundTask = researchTaskRepository.findById(savedTask.getId());
        // Assert
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("task1");
        assertThat(foundTask.get().isCompleted()).isTrue();
    }
    @Test
    void shouldFindAllTasks() {
        // Arrange
        ResearchTasks task1 = new ResearchTasks();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCompleted(false);
        researchTaskRepository.save(task1);

        ResearchTasks task2 = new ResearchTasks();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCompleted(true);
        researchTaskRepository.save(task2);
        // Act
        List<ResearchTasks> allTasks = researchTaskRepository.findAll();
        // Assert
        assertThat(allTasks).hasSize(2);
        assertThat(allTasks)
                .extracting(ResearchTasks::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }
    @Test
    void shouldUpdateTask() {
        // Arrange
        ResearchTasks task = new ResearchTasks();
        task.setTitle("Title");
        task.setDescription("Description");
        task.setCompleted(false);
        ResearchTasks savedTask = researchTaskRepository.save(task);
        // Act
        savedTask.setTitle("Updated Title");
        savedTask.setDescription("Updated Description");
        savedTask.setCompleted(true);
        ResearchTasks updatedTask = researchTaskRepository.save(savedTask);
        // Assert
        Optional<ResearchTasks> foundTask = researchTaskRepository.findById(updatedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Updated Title");
        assertThat(foundTask.get().getDescription()).isEqualTo("Updated Description");
        assertThat(foundTask.get().isCompleted()).isTrue();
    }
    @Test
    void shouldDeleteTask() {
        // Arrange
        ResearchTasks task = new ResearchTasks();
        task.setTitle("To Be Deleted");
        task.setDescription("Task description");
        ResearchTasks savedTask = researchTaskRepository.save(task);
        // Act
        researchTaskRepository.deleteById(savedTask.getId());
        // Assert
        Optional<ResearchTasks> deletedTask = researchTaskRepository.findById(savedTask.getId());
        assertThat(deletedTask).isEmpty();
    }
    @Test
    void shouldFilterCompletedTasksInMemory() {
        // Arrange
        ResearchTasks completedTask = new ResearchTasks();
        completedTask.setTitle("Completed Task");
        completedTask.setDescription("Completed description");
        completedTask.setCompleted(true);
        researchTaskRepository.save(completedTask);

        ResearchTasks incompleteTask = new ResearchTasks();
        incompleteTask.setTitle("Incomplete Task");
        incompleteTask.setDescription("Incomplete description");
        incompleteTask.setCompleted(false);
        researchTaskRepository.save(incompleteTask);
        // Act
        List<ResearchTasks> allTasks = researchTaskRepository.findAll();
        List<ResearchTasks> completedTasks = allTasks.stream()
                .filter(ResearchTasks::isCompleted)
                .toList();
        // Assert
        assertThat(completedTasks).hasSize(1);
    }
}