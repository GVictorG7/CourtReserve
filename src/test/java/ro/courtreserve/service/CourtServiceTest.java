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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testSaveCourt() {
        CourtDTO courtDTO = new CourtDTO(null, ADDRESS, null);
        Court mappedCourt = new Court(null, ADDRESS, null);
        when(mapper.map(courtDTO, Court.class)).thenReturn(mappedCourt);

        Court savedCourt = new Court(1L, ADDRESS, null);
        when(repository.save(mappedCourt)).thenReturn(savedCourt);
        CourtDTO savedCourtDTO = new CourtDTO(1L, ADDRESS, null);
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
        when(mapper.map(savedCourt,CourtDTO.class)).thenReturn(savedCourtDTO);

        CourtDTO actualResult = classUnderTest.setPriceForCourt(2L, priceDTO);
        assertEquals(savedCourtDTO, actualResult);
        verify(mapper).map(priceDTO, Price.class);
        verify(mapper).map(savedCourt, CourtDTO.class);
        verify(repository).save(court);
    }

    @Test
    void testGivenValidCourtIdWhenSetUpdatedPriceForCourtThenReturnPriceDTO() {
        List<Price> prices = List.of(new Price(null, Season.WINTER, null, Boolean.TRUE, DayPeriod.EVENING));
        Court court = new Court(null, null, prices);

        when(repository.findById(2L)).thenReturn(Optional.of(court));
        PriceDTO priceDTO = new PriceDTO();
        when(mapper.map(priceDTO, Price.class)).thenReturn(prices.get(0));

        Court savedCourt = new Court();
        when(repository.save(any(Court.class))).thenReturn(savedCourt);

        CourtDTO savedCourtDTO = new CourtDTO();
        when(mapper.map(savedCourt,CourtDTO.class)).thenReturn(savedCourtDTO);

        CourtDTO actualResult = classUnderTest.setPriceForCourt(2L, priceDTO);
        assertEquals(savedCourtDTO, actualResult);
        verify(mapper).map(priceDTO, Price.class);
        verify(mapper).map(savedCourt, CourtDTO.class);
        verify(repository).save(court);
    }

    @Test
    void testGivenInvalidCourtIdWhenDeletePriceForCourtThenReturnNull() {
        assertNull(classUnderTest.deletePriceOfCourt(2L, null));
    }

    @Test
    void testGivenValidCourtIdWhenDeletePriceForCourtThenReturnPriceDTO() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price(null, Season.WINTER, null, Boolean.TRUE, DayPeriod.EVENING));
        Court court = new Court(null, null, prices);

        when(repository.findById(2L)).thenReturn(Optional.of(court));
        PriceDTO priceDTO = new PriceDTO();
        when(mapper.map(priceDTO, Price.class)).thenReturn(prices.get(0));

        Court savedCourt = new Court();
        when(repository.save(any(Court.class))).thenReturn(savedCourt);

        CourtDTO savedCourtDTO = new CourtDTO();
        when(mapper.map(savedCourt,CourtDTO.class)).thenReturn(savedCourtDTO);

        CourtDTO actualResult = classUnderTest.deletePriceOfCourt(2L, priceDTO);
        assertEquals(savedCourtDTO, actualResult);
        assertEquals(0, prices.size());
        verify(mapper).map(priceDTO, Price.class);
        verify(mapper).map(savedCourt, CourtDTO.class);
        verify(repository).save(court);
    }
}