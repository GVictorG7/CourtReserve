package ro.courtreserve.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.courtreserve.controller.CourtController;
import ro.courtreserve.model.DayPeriod;
import ro.courtreserve.model.Season;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.model.entities.Price;
import ro.courtreserve.repository.ICourtRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CourtControllerIntegrationTest {
    private static final String COURT_ENDPOINT = "/court/";
    private static final String PRICE_ENDPOINT = "/price";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ADDRESS = "Address";
    private MockMvc mockMvc;
    private Court court;

    @Autowired
    private CourtController classUnderTest;
    @Autowired
    private ICourtRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest).build();
        List<Price> prices = List.of(new Price(1L, Season.WINTER, 10F, Boolean.TRUE, DayPeriod.MORNING));
        court = repository.save(new Court(1L, ADDRESS, prices));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testGetCourts() throws Exception {
        CourtDTO courtDTO = modelMapper.map(court, CourtDTO.class);
        mockMvc.perform(
                        get(COURT_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(List.of(courtDTO))));
    }

    @Test
    void testSaveCourt() throws Exception {
        CourtDTO courtDTO = new CourtDTO(2L, "Address2", null);
        mockMvc.perform(
                        post(COURT_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(courtDTO)))
                .andExpect(status().isOk());
        assertEquals(2L, repository.count());
    }

    @Test
    void testDeleteCourt() throws Exception {
        mockMvc.perform(
                        delete(COURT_ENDPOINT + court.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(0L, repository.count());
    }

    @Test
    void testGivenValidCourtIdWhenSetPriceThenReturnOk() throws Exception {
        PriceDTO priceDTO = new PriceDTO(Season.WINTER, 20F, Boolean.TRUE, DayPeriod.MORNING);
        mockMvc.perform(
                        post(COURT_ENDPOINT + court.getId().toString() + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isOk());
        Court fetchedCourt = repository.findById(court.getId()).orElseThrow();
        assertEquals(1, fetchedCourt.getPrices().size());
        assertEquals(20F, fetchedCourt.getPrices().get(0).getValue());
    }

    @Test
    void testGivenValidCourtIdWhenSetNewPriceThenReturnOk() throws Exception {
        PriceDTO priceDTO = new PriceDTO(Season.WINTER, 20F, Boolean.TRUE, DayPeriod.EVENING);
        mockMvc.perform(
                        post(COURT_ENDPOINT + court.getId().toString() + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isOk());
        Court fetchedCourt = repository.findById(court.getId()).orElseThrow();
        assertEquals(2, fetchedCourt.getPrices().size());
        assertEquals(20F, fetchedCourt.getPrices().get(1).getValue());
    }

    @Test
    void testGivenInvalidCourtIdWhenSetPriceThenReturnNotFound() throws Exception {
        mockMvc.perform(
                        post(COURT_ENDPOINT + "0" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(new PriceDTO())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGivenValidCourtIdWhenDeletePriceThenReturnOk() throws Exception {
        PriceDTO priceDTO = new PriceDTO(Season.WINTER, 20F, Boolean.TRUE, DayPeriod.MORNING);
        mockMvc.perform(
                        delete(COURT_ENDPOINT + court.getId().toString() + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isOk());
        Court fetchedCourt = repository.findById(court.getId()).orElseThrow();
        assertTrue(fetchedCourt.getPrices().isEmpty());
    }

    @Test
    void testGivenInvalidCourtIdWhenDeletePriceThenReturnNotFound() throws Exception {
        mockMvc.perform(
                        delete(COURT_ENDPOINT + "0" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(new PriceDTO())))
                .andExpect(status().isNotFound());
        Court fetchedCourt = repository.findById(court.getId()).orElseThrow();
        assertEquals(1, fetchedCourt.getPrices().size());
    }
}
