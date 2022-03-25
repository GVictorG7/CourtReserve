package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.UserDTO;
import ro.courtreserve.service.UserService;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    /**
     * The Log in endpoint. Should receive a {@link UserDTO} object with only the username and password fields set
     *
     * @param user the {@link UserDTO} with the login credentials
     * @return status code 200 and the {@link UserDTO} with the rest of the details if the login succeeded or status
     * code 403 if the login failed
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTO> logIn(@RequestBody UserDTO user) {
        UserDTO signedInUser = service.singIn(user);
        HttpStatus status = signedInUser == null ? FORBIDDEN : OK;
        return ResponseEntity.status(status).body(signedInUser);
    }
}
