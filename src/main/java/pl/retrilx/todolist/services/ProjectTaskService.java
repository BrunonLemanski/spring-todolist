package pl.retrilx.todolist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.retrilx.todolist.domain.Backlog;
import pl.retrilx.todolist.domain.Project;
import pl.retrilx.todolist.domain.ProjectTask;
import pl.retrilx.todolist.exceptions.ProjectNotFoundException;
import pl.retrilx.todolist.repository.BacklogRepository;
import pl.retrilx.todolist.repository.ProjectRepository;
import pl.retrilx.todolist.repository.ProjectTasksRepository;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTasksRepository projectTasksRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //ProjectTask to be added to a specific project if project is not null, project is not null when backlog exist
        //set the backlog to the projectTask

        // I use projectSequence because i want know what tasks are wiring with which projects. the sequence can look like
        //  this: PROID-1 for one task, another one PROID-2 etc... the program get projectId and put this in the sequence.
        //  Number of task depends of position in projectsTasks list.


        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);

            Integer BacklogSequence = backlog.getPTSequence();
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);

            //Add Sequence to projectTask
            projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority is null
            if (projectTask.getPriority() == null) {
                projectTask.setPriority(3); //if someone dont set priority then we can say, the task is not important
            }

            //INITIAL status when status is null
            if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
                projectTask.setStatus("NIE_WYKONANO");
            }

            return projectTasksRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Projekt "+ projectIdentifier +" nie istnieje w bazie!");
        }
    }

    public Iterable<ProjectTask>findBacklogById(String id){

        Project project = projectRepository.findByProjectIdentifier(id);

        if(project == null){
            throw new ProjectNotFoundException("Projekt z ID " + id + " nie istnieje");
        }

        return projectTasksRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure you are searching it on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);

        //make sure the backlog exists
        if(backlog == null){
            throw new ProjectNotFoundException("Projekt z ID '" + backlog_id + "' nie istnieje");
        }

        ProjectTask projectTask = projectTasksRepository.findByProjectSequence(pt_id);

        //make sure the projectTask exists
        if(projectTask == null){
            throw new ProjectNotFoundException("Zadanie projektowe '" + pt_id + "' nie istnieje");
        }

        //make sure the projectTask is assigned to the right backlog
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Zadanie projektowe '" + pt_id + "' nie istnieje w projekcie: " + backlog_id);
        }

        return projectTasksRepository.findByProjectSequence(pt_id);
    }

    public ProjectTask updateByProjectSequence(ProjectTask updateTask,String backlog_id, String pt_id){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTask = updateTask;

        return projectTasksRepository.save(updateTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTasksRepository.delete(projectTask);
    }
}
