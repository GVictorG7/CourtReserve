package ro.courtreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.repository.ICourtRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(repository.save(court)).thenReturn(null);

        classUnderTest.saveCourt(courtDTO);
        verify(mapper).map(courtDTO, Court.class);
        verify(repository).save(court);
    }

    @Test
    void testDeleteCourt() {
        classUnderTest.deleteCourt(1L);
        verify(repository).deleteById(1L);
    }
}