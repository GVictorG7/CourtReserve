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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourtServiceTest {
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
        CourtDTO courtDTO = new CourtDTO();
        Court court = new Court();
        when(mapper.map(courtDTO, Court.class)).thenReturn(court);

        classUnderTest.saveCourt(courtDTO);
        verify(mapper).map(courtDTO, Court.class);
        verify(repository).save(court);
    }

    @Test
    void testDeleteCourt() {
        classUnderTest.deleteCourt(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testGivenInvalidCourtIdWhenSetPriceForCourtThenReturnFalse() {
        assertFalse(classUnderTest.setPriceForCourt(2L, null));
    }

    @Test
    void testGivenValidCourtIdWhenSetNewPriceForCourtThenReturnTrue() {
        Court court = new Court();
        when(repository.findById(any())).thenReturn(Optional.of(court));
        PriceDTO priceDTO = new PriceDTO();

        assertTrue(classUnderTest.setPriceForCourt(2L, priceDTO));
        verify(mapper).map(priceDTO, Price.class);
        verify(repository).save(court);
    }

    @Test
    void testGivenValidCourtIdWhenSetUpdatedPriceForCourtThenReturnTrue() {
        List<Price> prices = List.of(new Price(null, Season.WINTER, null, Boolean.TRUE, DayPeriod.EVENING));
        Court court = new Court(null, null, prices);

        when(repository.findById(2L)).thenReturn(Optional.of(court));
        PriceDTO priceDTO = new PriceDTO();
        when(mapper.map(priceDTO, Price.class)).thenReturn(prices.get(0));

        assertTrue(classUnderTest.setPriceForCourt(2L, priceDTO));
        verify(mapper).map(priceDTO, Price.class);
        verify(repository).save(court);
    }

    @Test
    void testGivenInvalidCourtIdWhenDeletePriceForCourtThenReturnFalse() {
        assertFalse(classUnderTest.deletePriceOfCourt(2L, null));
    }

    @Test
    void testGivenValidCourtIdWhenDeletePriceForCourtThenReturnTrue() {
        List<Price> prices = new ArrayList<>();
        prices.add(new Price(null, Season.WINTER, null, Boolean.TRUE, DayPeriod.EVENING));
        Court court = new Court(null, null, prices);

        when(repository.findById(2L)).thenReturn(Optional.of(court));
        PriceDTO priceDTO = new PriceDTO();
        when(mapper.map(priceDTO, Price.class)).thenReturn(prices.get(0));

        assertTrue(classUnderTest.deletePriceOfCourt(2L, priceDTO));
        assertEquals(0, prices.size());
        verify(mapper).map(priceDTO, Price.class);
        verify(repository).save(court);
    }

}