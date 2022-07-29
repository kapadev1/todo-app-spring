package io.github.bulwa1.logic;

import io.github.bulwa1.model.TaskGroup;
import io.github.bulwa1.model.TaskGroupRepository;
import io.github.bulwa1.model.TaskRepository;
import io.github.bulwa1.model.projection.GroupReadModel;
import io.github.bulwa1.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {
   private TaskGroupRepository repository;
   private TaskRepository taskRepository;

   private int count;

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel creatGroup(final GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not fuond"));
        result.setDone(!result.isDone());
    }


}
