package pl.retrilx.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Podaj nazwę użytkownika")
    @Email(message = "Nazwa użytkownika musi być adresem email")
    @Column(unique = true, updatable = false)
    private String username;

    @NotBlank(message = "Podaj hasło użytkownika")
    private String password;

    @NotBlank(message = "Podaj imię i nazwisko")
    private String fullname;

    @Transient
    private String confirmPassword;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date created_At;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date updated_At;

    //Relation
    //OneToMany with Projects

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "user", orphanRemoval = true)
    private List<Project> projectList = new ArrayList<>();


    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Date getCreated_At() {
        return created_At;
    }

    public void setCreated_At(Date created_At) {
        this.created_At = created_At;
    }

    public Date getUpdated_At() {
        return updated_At;
    }

    public void setUpdated_At(Date updated_At) {
        this.updated_At = updated_At;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    @PrePersist
    protected void onCreate(){
        this.created_At = new Date();
    };

    @PreUpdate
    protected void onUpdate(){
        this.updated_At = new Date();
    }

    /*
    * UserDetails interface methods
    * */

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
