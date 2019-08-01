package pl.retrilx.todolist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.retrilx.todolist.domain.ProjectTask;

import java.util.List;

@Repository
public interface ProjectTasksRepository extends CrudRepository<ProjectTask, Long> {

    List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

    ProjectTask findByProjectSequence(String projectSequence);

}
