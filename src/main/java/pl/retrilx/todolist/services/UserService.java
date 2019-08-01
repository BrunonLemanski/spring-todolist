package pl.retrilx.todolist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.retrilx.todolist.domain.User;
import pl.retrilx.todolist.exceptions.UserUsernameException;
import pl.retrilx.todolist.repository.UserRepository;
import pl.retrilx.todolist.validator.UserValidator;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //also add Bean in main class

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            //username has to be unique - check
            newUser.setUsername(newUser.getUsername());

            //make sure that password and confirmPassword match

            //we dont persist and show confirmPassword
            newUser.setConfirmPassword("");

            return userRepository.save(newUser);
        }catch (Exception e){
            throw new UserUsernameException("Użytkownik " + newUser.getUsername() + " już istnieje");
        }
    }
}
