package pl.retrilx.todolist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.retrilx.todolist.domain.ProjectTask;

@Repository
public interface ProjectTasksRepository extends CrudRepository<ProjectTask, Long> {
}
