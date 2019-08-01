package pl.retrilx.todolist.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.retrilx.todolist.domain.User;


@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        User user = (User)o;

        if(user.getPassword().length() < 6){
            errors.rejectValue("password", "Length", "Hasło musi zawierać conajmniej 6 znaków");
        }

        if(!user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "Match", "Hasła nie są takie same");
        }
    }
}
