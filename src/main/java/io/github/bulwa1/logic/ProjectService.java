package io.github.bulwa1.logic;

import io.github.bulwa1.TaskConfigurationProperties;
import io.github.bulwa1.model.*;
import io.github.bulwa1.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel creatGroup(LocalDateTime deadline, int projectId) {
        if (!config.isAllowMultipleTasksFromTemplate() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(projectStep -> new Task(
                                    projectStep.getDescription(),
                                    deadline.plusDays(projectStep.getDaysToDeadline()))
                            ).collect(Collectors.toSet())
                    );
                    return targetGroup;
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
