package pl.retrilx.todolist.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    private String username; //must be the same like variable in entity class User

    @NotBlank(message = "Hasło jest wymagane")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
