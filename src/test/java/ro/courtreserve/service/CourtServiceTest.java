package ro.courtreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ro.courtreserve.model.DayPeriod;
import ro.courtreserve.model.Season;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.model.entities.Price;
import ro.courtreserve.repository.ICourtRepository;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classUnderTest = new CourtService(repository, mapper);
    }

    @Test
    void testGetAllCourts() {
        when(repository.findAll()).thenReturn(List.of());
        assertEquals(List.of(), classUnderTest.getAllCourts());
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

        CourtDTO actualResult = classUnderTest.getCourtById(id);
        assertNull(actualResult);
    }

    @Test
    void testSaveCourt() {
        CourtDTO courtDTO = new CourtDTO(null, ADDRESS, null, null);
        Court mappedCourt = new Court(null, ADDRESS, null, null);
        when(mapper.map(courtDTO, Court.class)).thenReturn(mappedCourt);

        Court savedCourt = new Court(1L, ADDRESS, null, null);
        when(repository.save(mappedCourt)).thenReturn(savedCourt);
        CourtDTO savedCourtDTO = new CourtDTO(1L, ADDRESS, null, null);
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
        Court court = new Court(null, null, Set.of(), null);
        when(repository.findById(1L)).thenReturn(Optional.of(court));
        assertEquals(Set.of(), classUnderTest.getAllPricesForCourt(1L));
    }

    @Test
    void testGivenInvalidCourtIdWhenGetPriceByIdThenThrow() {
        assertThrows(NoSuchElementException.class, () -> classUnderTest.getPriceById(1L, 1L));
    }

    @Test
    void testGivenValidCourtIdAndInvalidPriceIdWhenGetPriceByIdThenThrow() {
        Court court = new Court(null, null, Set.of(), null);
        when(repository.findById(1L)).thenReturn(Optional.of(court));
        assertThrows(NoSuchElementException.class, () -> classUnderTest.getPriceById(1L, 1L));
    }

    @Test
    void testGivenValidCourtIdAndValidPriceIdWhenGetPriceByIdThenReturnPriceDTO() {
        Price price = new Price(1L, null, null, null, null);
        Court court = new Court(null, null, Set.of(price), null);
        when(repository.findById(1L)).thenReturn(Optional.of(court));

        PriceDTO priceDTO = new PriceDTO();
        when(mapper.map(price, PriceDTO.class)).thenReturn(priceDTO);

        PriceDTO actualResult = classUnderTest.getPriceById(1L, 1L);
        assertEquals(priceDTO, actualResult);
    }

    @Test
    void testGivenInvalidCourtIdWhenSetPriceForCourtThenReturnNull() {
        assertNull(classUnderTest.setPriceForCourt(2L, null));
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
        Price price = new Price(null, Season.WINTER, null, Boolean.TRUE, DayPeriod.EVENING);
        Set<Price> prices = Set.of(price);
        Court court = new Court(null, null, prices, null);

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
        Court court = new Court(2L, null, new HashSet<>(), null);
        when(repository.findById(2L)).thenReturn(Optional.of(court));
        assertThrows(NoSuchElementException.class, () -> classUnderTest.deletePriceOfCourt(2L, null));
    }

    @Test
    void testGivenValidCourtIdAndValidPriceIdWhenDeletePriceForCourtThenNotThrow() {
        Price price = new Price(1L, null, null, null, null);
        Set<Price> prices = new HashSet<>();
        prices.add(price);
        Court court = new Court(1L, null, prices, null);

        when(repository.findById(1L)).thenReturn(Optional.of(court));

        assertDoesNotThrow(() -> classUnderTest.deletePriceOfCourt(1L, 1L));
        court.setPrices(Set.of());
        verify(repository).save(court);
    }
}