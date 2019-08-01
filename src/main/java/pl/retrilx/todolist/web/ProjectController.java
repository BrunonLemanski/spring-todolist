package pl.retrilx.todolist.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.retrilx.todolist.domain.Project;
import pl.retrilx.todolist.services.MapValidationErrorService;
import pl.retrilx.todolist.services.ProjectService;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidatonService(result);
        if(errorMap!=null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }


    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){

        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }


    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){return projectService.findAllProjects(principal.getName());}


    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<String>("Project z ID: '"+projectId+"' został usunięty", HttpStatus.OK);
    }
}