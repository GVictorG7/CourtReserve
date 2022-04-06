package ro.courtreserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.service.CourtService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CourtControllerTest {
    private static final String COURT_ENDPOINT = "/court/";
    private static final String PRICE_ENDPOINT = "/price";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock
    private CourtService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CourtController classUnderTest = new CourtController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest).build();
    }

    @Test
    void testGetCourts() throws Exception {
        List<CourtDTO> courtDTOs = List.of(new CourtDTO());
        when(service.getAllCourts()).thenReturn(courtDTOs);

        mockMvc.perform(
                        get(COURT_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(courtDTOs)));
        verify(service).getAllCourts();
    }

    @Test
    void testGivenValidIdWhenGetCourtByIdThenReturnCourtDTO() throws Exception {
        Long id = 1L;
        CourtDTO courtDTO = new CourtDTO();
        when(service.getCourtById(id)).thenReturn(courtDTO);

        mockMvc.perform(
                        get(COURT_ENDPOINT + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(courtDTO)));
        verify(service).getCourtById(1L);
    }

    @Test
    void testGivenInvalidIdWhenGetCourtByIdThenReturnNull() throws Exception {
        Long id = 1L;
        when(service.getCourtById(id)).thenReturn(null);

        mockMvc.perform(
                        get(COURT_ENDPOINT + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        verify(service).getCourtById(1L);
    }

    @Test
    void testSaveCourt() throws Exception {
        CourtDTO courtDTO = new CourtDTO();
        CourtDTO savedCourtDTO = new CourtDTO();
        when(service.saveCourt(courtDTO)).thenReturn(savedCourtDTO);

        mockMvc.perform(
                        post(COURT_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(courtDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(savedCourtDTO)));
        verify(service).saveCourt(courtDTO);
    }

    @Test
    void testDeleteCourt() throws Exception {
        mockMvc.perform(
                        delete(COURT_ENDPOINT + "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).deleteCourt(1L);
    }

    @Test
    void testGivenValidCourtIdWhenSetPriceThenReturnOk() throws Exception {
        PriceDTO priceDTO = new PriceDTO();
        CourtDTO savedCourtDTO = new CourtDTO();
        when(service.setPriceForCourt(1L, priceDTO)).thenReturn(savedCourtDTO);

        mockMvc.perform(
                        post(COURT_ENDPOINT + "1" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(savedCourtDTO)));
        verify(service).setPriceForCourt(1L, priceDTO);
    }

    @Test
    void testGivenInvalidCourtIdWhenSetPriceThenReturnNotFound() throws Exception {
        PriceDTO priceDTO = new PriceDTO();
        when(service.setPriceForCourt(1L, priceDTO)).thenReturn(null);

        mockMvc.perform(
                        post(COURT_ENDPOINT + "1" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        verify(service).setPriceForCourt(1L, priceDTO);
    }

    @Test
    void testGivenValidCourtIdWhenDeletePriceThenReturnOk() throws Exception {
        PriceDTO priceDTO = new PriceDTO();
        CourtDTO savedCourtDTO = new CourtDTO();
        when(service.deletePriceOfCourt(1L, priceDTO)).thenReturn(savedCourtDTO);

        mockMvc.perform(
                        delete(COURT_ENDPOINT + "1" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(savedCourtDTO)));
        verify(service).deletePriceOfCourt(1L, priceDTO);
    }

    @Test
    void testGivenInvalidCourtIdWhenDeletePriceThenReturnNotFound() throws Exception {
        PriceDTO priceDTO = new PriceDTO();
        when(service.deletePriceOfCourt(1L, priceDTO)).thenReturn(null);

        mockMvc.perform(
                        delete(COURT_ENDPOINT + "1" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        verify(service).deletePriceOfCourt(1L, priceDTO);
    }
}