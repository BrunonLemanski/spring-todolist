package pl.retrilx.todolist.exceptions;

public class ProjectIdExceptionResponse {

    private String projectId;

    public ProjectIdExceptionResponse(String projectId) {
        this.projectId = projectId;
    }

    public String getIdProject(){
        return projectId;
    }

    public void setIdProject(String idProject) {
        this.projectId = projectId;
    }
}
