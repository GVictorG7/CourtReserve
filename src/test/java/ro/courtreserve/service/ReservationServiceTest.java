package ro.courtreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.model.entities.Reservation;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IReservationRepository;
import ro.courtreserve.repository.IUserRepository;
import ro.courtreserve.util.MailService;
import ro.courtreserve.util.exception.CourtUnavailableException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ReservationServiceTest {
    private ReservationService classUnderTest;

    @Mock
    private IReservationRepository repository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private PriceService priceService;
    @Mock
    private MailService mailService;
    @Mock
    private CourtService courtService;
    @Mock
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classUnderTest = new ReservationService(repository, userRepository, priceService, mailService, courtService, mapper);
    }

    @Test
    void testGivenAvailableCourtWhenSaveReservationThenReturnPrice() {
        ReservationDTO rawReservationDTO = new ReservationDTO(
                null, (byte) 1, (byte) 2, 3, (byte) 4, 5L, Set.of(6L), null);
        when(courtService.isCourtAvailable(5L, (byte) 1, (byte) 2, 3, (byte) 4)).thenReturn(Boolean.TRUE);
        Reservation reservation = new Reservation();
        when(mapper.map(rawReservationDTO, Reservation.class)).thenReturn(reservation);
        User user = new User();
        user.setMail("mail");
        when(userRepository.findById(6L)).thenReturn(Optional.of(user));
        reservation.getUsers().add(user);
        Reservation savedReservation = new Reservation();
        when(repository.save(reservation)).thenReturn(savedReservation);
        ReservationDTO reservationDTO = new ReservationDTO();
        when(mapper.map(savedReservation, ReservationDTO.class)).thenReturn(reservationDTO);
        when(priceService.calculatePriceOfReservation(reservationDTO)).thenReturn(1F);

        Float price = assertDoesNotThrow(() -> classUnderTest.saveReservation(rawReservationDTO));
        assertEquals(1F, price);
        verify(mailService).sendEmail("mail", reservationDTO, 1F);
    }

    @Test
    void testGivenUnavailableCourtWhenSaveReservationThenThrow() {
        ReservationDTO rawReservationDTO = new ReservationDTO(
                null, (byte) 1, (byte) 2, 3, (byte) 4, 5L, null, null);
        when(courtService.isCourtAvailable(5L, (byte) 1, (byte) 2, 3, (byte) 4)).thenReturn(Boolean.FALSE);
        assertThrows(CourtUnavailableException.class, () -> classUnderTest.saveReservation(rawReservationDTO));
    }
}