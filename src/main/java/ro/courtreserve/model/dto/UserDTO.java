package ro.courtreserve.model.dto;

import lombok.Data;
import ro.courtreserve.model.UserRole;

@Data
public class UserDTO {
    private String username;
    private String password;
    private UserRole role;
    private String mail;
}
