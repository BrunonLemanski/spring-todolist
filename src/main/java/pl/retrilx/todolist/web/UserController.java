package pl.retrilx.todolist.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.retrilx.todolist.domain.User;
import pl.retrilx.todolist.payload.JWTLoginSuccessResponse;
import pl.retrilx.todolist.payload.LoginRequest;
import pl.retrilx.todolist.security.JwtTokenProvider;
import pl.retrilx.todolist.services.MapValidationErrorService;
import pl.retrilx.todolist.services.UserService;
import pl.retrilx.todolist.validator.UserValidator;

import javax.validation.Valid;
import javax.xml.ws.BindingType;

import static pl.retrilx.todolist.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        //Validate passwords match
        userValidator.validate(user, result);

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidatonService(result);
        if(errorMap != null){
            return errorMap;
        }

        User newUser = userService.saveUser(user);

        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidatonService(result);

        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwts = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwts));
    }
}
