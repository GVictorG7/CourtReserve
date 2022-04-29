package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.InvitationDTO;
import ro.courtreserve.service.InvitationService;

@Controller
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService service;

    @PostMapping("/")
    public ResponseEntity<Void> inviteUser(@RequestBody InvitationDTO invitation) {
        service.inviteUser(invitation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptInvitation(@RequestBody InvitationDTO invitation) {
        service.acceptInvitation(invitation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline")
    public ResponseEntity<Void> declineInvitation(@RequestBody InvitationDTO invitation) {
        service.declineInvitation(invitation);
        return ResponseEntity.ok().build();
    }
}
