package pl.retrilx.todolist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.retrilx.todolist.domain.Backlog;
import pl.retrilx.todolist.domain.Project;
import pl.retrilx.todolist.domain.User;
import pl.retrilx.todolist.exceptions.ProjectIdException;
import pl.retrilx.todolist.exceptions.ProjectNotFoundException;
import pl.retrilx.todolist.repository.BacklogRepository;
import pl.retrilx.todolist.repository.ProjectRepository;
import pl.retrilx.todolist.repository.UserRepository;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        try{

            //condition to prevent updating project by different user than owner
            if(project.getId() != null){
                Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

                if(existingProject != null && (!existingProject.getProjectLeader().equals(username))){
                    throw new ProjectNotFoundException("Nie znaleziono projektu w twoim zbiorze");
                }else if(existingProject == null){
                    throw new ProjectNotFoundException("Projekt '"+ project.getProjectIdentifier() +"' nie może być zaktualizowany, gdyż nie istnieje");
                }
            }


            //adding project for specific user
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);

        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' już istnieje");
        }

    }


    public Project findProjectByIdentifier(String projectId, String username){

        //Only want to return the project if the user looking for it is the owner

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());


        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"' nie istnieje");
        }

        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }



        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }


    public void deleteProjectByIdentifier(String projectid, String username){

        projectRepository.delete(findProjectByIdentifier(projectid, username));
    }

}