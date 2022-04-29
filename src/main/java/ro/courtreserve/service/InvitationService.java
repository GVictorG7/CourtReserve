package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.dto.InvitationDTO;
import ro.courtreserve.model.entities.Invitation;
import ro.courtreserve.model.entities.Reservation;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IInvitationRepository;
import ro.courtreserve.repository.IReservationRepository;
import ro.courtreserve.repository.IUserRepository;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final IInvitationRepository invitationRepository;
    private final IUserRepository userRepository;
    private final IReservationRepository reservationRepository;

    public void inviteUser(InvitationDTO invitationDTO) {
        User fromUser = userRepository.findById(invitationDTO.getFromUserId()).orElseThrow();
        User toUser = userRepository.findById(invitationDTO.getToUserId()).orElseThrow();
        Reservation reservation = reservationRepository.findById(invitationDTO.getReservationId()).orElseThrow();
        invitationRepository.save(new Invitation(fromUser, toUser, reservation));
    }

    public void acceptInvitation(InvitationDTO invitationDTO) {
        User fromUser = userRepository.findById(invitationDTO.getFromUserId()).orElseThrow();
        User toUser = userRepository.findById(invitationDTO.getToUserId()).orElseThrow();
        Reservation reservation = reservationRepository.findById(invitationDTO.getReservationId()).orElseThrow();
        Invitation invitation = invitationRepository.findByFromAndToAndReservation(fromUser, toUser, reservation);
        reservation.getUsers().add(toUser);
        reservationRepository.save(reservation);
        invitationRepository.deleteById(invitation.getId());
    }

    public void declineInvitation(InvitationDTO invitationDTO) {
        User fromUser = userRepository.findById(invitationDTO.getFromUserId()).orElseThrow();
        User toUser = userRepository.findById(invitationDTO.getToUserId()).orElseThrow();
        Reservation reservation = reservationRepository.findById(invitationDTO.getReservationId()).orElseThrow();
        Invitation invitation = invitationRepository.findByFromAndToAndReservation(fromUser, toUser, reservation);
        invitationRepository.deleteById(invitation.getId());
    }
}
