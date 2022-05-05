package ro.courtreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.model.entities.Price;
import ro.courtreserve.model.entities.Subscription;
import ro.courtreserve.repository.ICourtRepository;
import ro.courtreserve.repository.IReservationRepository;
import ro.courtreserve.repository.ISubscriptionRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourtServiceTest {
    private static final String ADDRESS = "Address";
    private CourtService classUnderTest;

    @Mock
    private ICourtRepository repository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private IReservationRepository reservationRepository;
    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classUnderTest = new CourtService(repository, reservationRepository, subscriptionRepository, mapper);
    }

    @Test
    void testGetAllCourts() {
        List<Court> courts = List.of(new Court());
        when(repository.findAll()).thenReturn(courts);
        when(mapper.map(courts.get(0), CourtDTO.class)).thenReturn(new CourtDTO());
        assertEquals(List.of(new CourtDTO()), classUnderTest.getAllCourts());
    }

    @Test
    void testGivenValidIdWhenGetCourtByIdThenReturnCourtDTO() {
        CourtDTO courtDTO = new CourtDTO();
        Court court = new Court();
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.of(court));
        when(mapper.map(court, CourtDTO.class)).thenReturn(courtDTO);

        CourtDTO actualResult = classUnderTest.getCourtById(id);
        assertEquals(courtDTO, actualResult);
    }

    @Test
    void testGivenInvalidIdWhenGetCourtByIdThenReturnNull() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(mapper.map(null, CourtDTO.class)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> classUnderTest.getCourtById(id));
    }

    @Test
    void testSaveCourt() {
        CourtDTO courtDTO = new CourtDTO(null, ADDRESS);
        Court mappedCourt = new Court(null, ADDRESS);
        when(mapper.map(courtDTO, Court.class)).thenReturn(mappedCourt);

        Court savedCourt = new Court(1L, ADDRESS);
        when(repository.save(mappedCourt)).thenReturn(savedCourt);
        CourtDTO savedCourtDTO = new CourtDTO(1L, ADDRESS);
        when(mapper.map(savedCourt, CourtDTO.class)).thenReturn(savedCourtDTO);

        CourtDTO actualResult = classUnderTest.saveCourt(courtDTO);
        assertEquals(savedCourtDTO, actualResult);
        verify(mapper).map(courtDTO, Court.class);
        verify(mapper).map(savedCourt, CourtDTO.class);
        verify(repository).save(mappedCourt);
    }

    @Test
    void testDeleteCourt() {
        classUnderTest.deleteCourt(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testGivenInvalidCourtIdWhenGetAllPricesForCourtThenThrows() {
        assertThrows(NoSuchElementException.class, () -> classUnderTest.getAllPricesForCourt(1L));
    }

    @Test
    void testGivenValidCourtIdWhenGetAllPricesForCourtThenReturn() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Court()));
        assertEquals(Set.of(), classUnderTest.getAllPricesForCourt(1L));
    }

    @Test
    void testGivenInvalidCourtIdWhenGetPriceByIdThenThrow() {
        assertThrows(NoSuchElementException.class, () -> classUnderTest.getPriceById(1L, 1L));
    }

    @Test
    void testGivenValidCourtIdAndInvalidPriceIdWhenGetPriceByIdThenThrow() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Court()));
        assertThrows(NoSuchElementException.class, () -> classUnderTest.getPriceById(1L, 1L));
    }

    @Test
    void testGivenValidCourtIdAndValidPriceIdWhenGetPriceByIdThenReturnPriceDTO() {
        Price price = new Price(1L, null, null, null, null);
        Court court = new Court();
        court.getPrices().add(price);
        when(repository.findById(1L)).thenReturn(Optional.of(court));

        PriceDTO priceDTO = new PriceDTO();
        when(mapper.map(price, PriceDTO.class)).thenReturn(priceDTO);

        PriceDTO actualResult = classUnderTest.getPriceById(1L, 1L);
        assertEquals(priceDTO, actualResult);
    }

    @Test
    void testGivenInvalidCourtIdWhenSetPriceForCourtThenReturnNull() {
        assertThrows(NoSuchElementException.class, () -> classUnderTest.setPriceForCourt(2L, null));
    }

    @Test
    void testGivenValidCourtIdWhenSetNewPriceForCourtThenReturnPriceDTO() {
        Court court = new Court();
        when(repository.findById(any())).thenReturn(Optional.of(court));
        PriceDTO priceDTO = new PriceDTO();

        Court savedCourt = new Court();
        when(repository.save(any(Court.class))).thenReturn(savedCourt);

        CourtDTO savedCourtDTO = new CourtDTO();
        when(mapper.map(savedCourt, CourtDTO.class)).thenReturn(savedCourtDTO);

        CourtDTO actualResult = classUnderTest.setPriceForCourt(2L, priceDTO);
        assertEquals(savedCourtDTO, actualResult);
        verify(mapper).map(priceDTO, Price.class);
        verify(mapper).map(savedCourt, CourtDTO.class);
        verify(repository).save(court);
    }

    @Test
    void testGivenValidCourtIdWhenSetUpdatedPriceForCourtThenReturnPriceDTO() {
        Price price = new Price(1L, null, null, null, null);
        Court court = new Court();
        court.getPrices().add(price);

        when(repository.findById(2L)).thenReturn(Optional.of(court));
        PriceDTO priceDTO = new PriceDTO();
        when(mapper.map(priceDTO, Price.class)).thenReturn(price);

        Court savedCourt = new Court();
        when(repository.save(any(Court.class))).thenReturn(savedCourt);

        CourtDTO savedCourtDTO = new CourtDTO();
        when(mapper.map(savedCourt, CourtDTO.class)).thenReturn(savedCourtDTO);

        CourtDTO actualResult = classUnderTest.setPriceForCourt(2L, priceDTO);
        assertEquals(savedCourtDTO, actualResult);
        verify(mapper).map(priceDTO, Price.class);
        verify(mapper).map(savedCourt, CourtDTO.class);
        verify(repository).save(court);
    }

    @Test
    void testGivenInvalidCourtIdWhenDeletePriceForCourtThenThrows() {
        assertThrows(NoSuchElementException.class, () -> classUnderTest.deletePriceOfCourt(2L, null));
    }

    @Test
    void testGivenValidCourtIdAndInvalidPriceIdWhenDeletePriceForCourtThenThrows() {
        Court court = new Court(2L, null);
        when(repository.findById(2L)).thenReturn(Optional.of(court));
        assertThrows(NoSuchElementException.class, () -> classUnderTest.deletePriceOfCourt(2L, null));
    }

    @Test
    void testGivenValidCourtIdAndValidPriceIdWhenDeletePriceForCourtThenNotThrow() {
        Price price = new Price(1L, null, null, null, null);
        Court court = new Court(1L, null);
        court.getPrices().add(price);

        when(repository.findById(1L)).thenReturn(Optional.of(court));

        assertDoesNotThrow(() -> classUnderTest.deletePriceOfCourt(1L, 1L));
        court.setPrices(Set.of());
        verify(repository).save(court);
    }

    @Test
    void testGivenInvalidCourtIdWhenIsCourtAvailableThenThrow() {
        when(repository.findById(1L)).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class,
                () -> classUnderTest.isCourtAvailable(1L, null, null, null, null));
    }

    @Test
    void testGivenValidCourtIdAndNonExistingConflictsWhenIsCourtAvailableThenReturnTrue() {
        Byte day = null;
        Byte month = null;
        Integer year = null;
        Byte hour = null;
        Court court = new Court();
        when(repository.findById(1L)).thenReturn(Optional.of(court));
        when(reservationRepository.findAllByCourtAndDayAndMonthAndYearAndHour(court, day, month, year, hour)).thenReturn(List.of());
        when(subscriptionRepository.findAllByCourtAndStartDateDayAndStartDateMonthAndStartDateYearAndStartHour(court, day, month, year, hour)).thenReturn(List.of());
        assertTrue(classUnderTest.isCourtAvailable(1L, day, month, year, hour));
    }

    @Test
    void testGivenValidCourtIdAndExistingConflictsWhenIsCourtAvailableThenReturnFalse() {
        Byte day = null;
        Byte month = null;
        Integer year = null;
        Byte hour = null;
        Court court = new Court();
        when(repository.findById(1L)).thenReturn(Optional.of(court));
        when(reservationRepository.findAllByCourtAndDayAndMonthAndYearAndHour(court, day, month, year, hour)).thenReturn(List.of());
        when(subscriptionRepository.findAllByCourtAndStartDateDayAndStartDateMonthAndStartDateYearAndStartHour(court, day, month, year, hour)).thenReturn(List.of(new Subscription()));
        assertFalse(classUnderTest.isCourtAvailable(1L, day, month, year, hour));
    }
}