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

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> logIn(@RequestBody UserDTO user) {
        UserDTO signedInUser = service.singIn(user);
        HttpStatus status = HttpStatus.OK;
        if (signedInUser == null) {
            status = HttpStatus.FORBIDDEN;
        }
        return new ResponseEntity<>(signedInUser, status);
    }
}
