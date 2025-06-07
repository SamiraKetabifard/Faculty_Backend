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
        // Create new task using setters
        ResearchTasks task = new ResearchTasks();
        task.setTitle("New Research");
        task.setDescription("Research Description");
        task.setCompleted(false);

        // Save the task
        ResearchTasks savedTask = researchTaskRepository.save(task);

        // Verify the saved task
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isPositive();
        assertThat(savedTask.getTitle()).isEqualTo("New Research");
        assertThat(savedTask.getDescription()).isEqualTo("Research Description");
        assertThat(savedTask.isCompleted()).isFalse();
    }

    @Test
    void shouldFindTaskById() {
        // Create and save a task
        ResearchTasks task = new ResearchTasks();
        task.setTitle("Find Me");
        task.setDescription("Task to find");
        task.setCompleted(true);
        ResearchTasks savedTask = researchTaskRepository.save(task);

        // Find the task by ID
        Optional<ResearchTasks> foundTask = researchTaskRepository.findById(savedTask.getId());

        // Verify the found task
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Find Me");
        assertThat(foundTask.get().isCompleted()).isTrue();
    }

    @Test
    void shouldFindAllTasks() {
        // Create and save multiple tasks
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

        // Get all tasks
        List<ResearchTasks> allTasks = researchTaskRepository.findAll();

        // Verify
        assertThat(allTasks).hasSize(2);
        assertThat(allTasks)
                .extracting(ResearchTasks::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void shouldUpdateTask() {
        // Create and save initial task
        ResearchTasks task = new ResearchTasks();
        task.setTitle("Original Title");
        task.setDescription("Original Description");
        task.setCompleted(false);
        ResearchTasks savedTask = researchTaskRepository.save(task);

        // Update the task
        savedTask.setTitle("Updated Title");
        savedTask.setDescription("Updated Description");
        savedTask.setCompleted(true);
        ResearchTasks updatedTask = researchTaskRepository.save(savedTask);

        // Verify the update
        Optional<ResearchTasks> foundTask = researchTaskRepository.findById(updatedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Updated Title");
        assertThat(foundTask.get().getDescription()).isEqualTo("Updated Description");
        assertThat(foundTask.get().isCompleted()).isTrue();
    }

    @Test
    void shouldDeleteTask() {
        // Create and save a task
        ResearchTasks task = new ResearchTasks();
        task.setTitle("To Be Deleted");
        task.setDescription("Task description");
        ResearchTasks savedTask = researchTaskRepository.save(task);

        // Delete the task
        researchTaskRepository.deleteById(savedTask.getId());

        // Verify deletion
        Optional<ResearchTasks> deletedTask = researchTaskRepository.findById(savedTask.getId());
        assertThat(deletedTask).isEmpty();
    }

    @Test
    void shouldFilterCompletedTasksInMemory() {
        // Create and save tasks with different completion statuses
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

        // Get all tasks and filter in memory
        List<ResearchTasks> allTasks = researchTaskRepository.findAll();
        List<ResearchTasks> completedTasks = allTasks.stream()
                .filter(ResearchTasks::isCompleted)
                .toList();

        // Verify
        assertThat(completedTasks).hasSize(1);
        assertThat(completedTasks.get(0).getTitle()).isEqualTo("Completed Task");
    }
}