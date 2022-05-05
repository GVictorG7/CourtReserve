package ro.courtreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.courtreserve.model.dto.InvitationDTO;
import ro.courtreserve.model.entities.Invitation;
import ro.courtreserve.model.entities.Reservation;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IInvitationRepository;
import ro.courtreserve.repository.IReservationRepository;
import ro.courtreserve.repository.IUserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InvitationServiceTest {
    private InvitationService classUnderTest;

    @Mock
    private IInvitationRepository invitationRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classUnderTest = new InvitationService(invitationRepository, userRepository, reservationRepository);
    }

    @Test
    void inviteUser() {
        InvitationDTO invitation = new InvitationDTO(1L, 2L, 3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(reservationRepository.findById(3L)).thenReturn(Optional.of(new Reservation()));

        classUnderTest.inviteUser(invitation);
        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    void acceptInvitation() {
        InvitationDTO invitation = new InvitationDTO(1L, 2L, 3L);

        User fromUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(fromUser));
        User toUser = new User();
        when(userRepository.findById(2L)).thenReturn(Optional.of(toUser));
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));
        when(invitationRepository.findByFromAndToAndReservation(fromUser, toUser, reservation))
                .thenReturn(new Invitation(4L, null, null, null));

        classUnderTest.acceptInvitation(invitation);

        reservation.getUsers().add(toUser);
        verify(reservationRepository).save(reservation);
        verify(invitationRepository).deleteById(4L);
    }

    @Test
    void declineInvitation() {
        InvitationDTO invitation = new InvitationDTO(1L, 2L, 3L);

        User fromUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(fromUser));
        User toUser = new User();
        when(userRepository.findById(2L)).thenReturn(Optional.of(toUser));
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(3L)).thenReturn(Optional.of(reservation));
        when(invitationRepository.findByFromAndToAndReservation(fromUser, toUser, reservation))
                .thenReturn(new Invitation(4L, null, null, null));

        classUnderTest.declineInvitation(invitation);
        verify(invitationRepository).deleteById(4L);
    }
}