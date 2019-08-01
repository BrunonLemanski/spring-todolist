package pl.retrilx.todolist.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.retrilx.todolist.domain.Project;
import pl.retrilx.todolist.domain.ProjectTask;
import pl.retrilx.todolist.services.MapValidationErrorService;
import pl.retrilx.todolist.services.ProjectTaskService;

import javax.validation.Valid;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    //dodawanie zadania do backlogu
    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlog_id, Principal principal){
        //show delete
        //custom exception

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidatonService(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

    }


    //pobieranie backlogu
    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id, Principal principal){

        return projectTaskService.findBacklogById(backlog_id, principal.getName());
    }

    //pobieranie zadania dla backlog'u
    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){

        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<ProjectTask>( projectTask, HttpStatus.OK);
    }


    //aktualizowanie zadania dla backlog'u
    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlog_id, @PathVariable String pt_id, Principal principal ){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidatonService(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id,pt_id, principal.getName());

        return new ResponseEntity<ProjectTask>(updatedTask,HttpStatus.OK);

    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){
        projectTaskService.deletePTByProjectSequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<String>("Zadanie projektowe '" + pt_id + "' zostało usunięte", HttpStatus.OK);
    }
}
