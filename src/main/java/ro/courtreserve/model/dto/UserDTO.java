package ro.courtreserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.courtreserve.model.UserRole;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private UserRole role;
    private String mail;
    private Set<Long> reservationIds = new HashSet<>();
    private Set<Long> subscriptionIds = new HashSet<>();
    private Set<InvitationDTO> sentInvitation = new HashSet<>();
    private Set<InvitationDTO> receivedInvitation = new HashSet<>();

    public UserDTO(String username, String password, UserRole role, String mail) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mail = mail;
    }
}
