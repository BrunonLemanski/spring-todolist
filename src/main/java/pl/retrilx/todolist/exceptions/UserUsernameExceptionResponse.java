package pl.retrilx.todolist.exceptions;

public class UserUsernameExceptionResponse {

    private String username;

    public UserUsernameExceptionResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
