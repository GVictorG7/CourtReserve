package ro.courtreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ro.courtreserve.model.dto.SubscriptionDTO;
import ro.courtreserve.model.entities.Subscription;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.ISubscriptionRepository;
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

class SubscriptionServiceTest {
    private SubscriptionService classUnderTest;

    @Mock
    private ISubscriptionRepository subscriptionRepository;
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
        classUnderTest = new SubscriptionService(
                subscriptionRepository, userRepository, priceService, mailService, courtService, mapper);
    }

    @Test
    void testGivenAvailableCourtWhenSaveReservationThenReturnPrice() {
        SubscriptionDTO rawSubscriptionDTO = new SubscriptionDTO(
                null, (byte) 1, (byte) 2, 3, (byte) 4, (byte) 5, 6L, Set.of(7L));
        when(courtService.isCourtAvailable(6L, (byte) 1, (byte) 2, 3, (byte) 4)).thenReturn(Boolean.TRUE);
        Subscription subscription = new Subscription();
        when(mapper.map(rawSubscriptionDTO, Subscription.class)).thenReturn(subscription);
        User user = new User();
        user.setMail("mail");
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        subscription.getUsers().add(user);
        Subscription savedSubscription = new Subscription();
        when(subscriptionRepository.save(subscription)).thenReturn(savedSubscription);
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        when(mapper.map(savedSubscription, SubscriptionDTO.class)).thenReturn(subscriptionDTO);
        when(priceService.calculateTotalPriceOfSubscription(subscriptionDTO)).thenReturn(1F);

        Float price = assertDoesNotThrow(() -> classUnderTest.saveSubscription(rawSubscriptionDTO));
        assertEquals(1F, price);
        verify(mailService).sendEmail("mail", subscriptionDTO, 1F);
    }

    @Test
    void testGivenUnavailableCourtWhenSaveReservationThenThrow() {
        SubscriptionDTO rawSubscriptionDTO = new SubscriptionDTO(
                null, (byte) 1, (byte) 2, 3, (byte) 4, (byte) 5, 6L, Set.of(7L));
        when(courtService.isCourtAvailable(5L, (byte) 1, (byte) 2, 3, (byte) 4)).thenReturn(Boolean.FALSE);
        assertThrows(CourtUnavailableException.class, () -> classUnderTest.saveSubscription(rawSubscriptionDTO));
    }
}