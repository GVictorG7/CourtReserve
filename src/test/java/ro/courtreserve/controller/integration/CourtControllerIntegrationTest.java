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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Set<Price> prices = Set.of(new Price(1L, Season.WINTER, 10F, Boolean.TRUE, DayPeriod.MORNING));
        court = repository.save(new Court(1L, ADDRESS, prices, new HashSet<>()));
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
    void testGivenValidIdWhenGetCourtByIdThenReturnCourtDTO() throws Exception {
        Long id = court.getId();
        CourtDTO courtDTO = modelMapper.map(court, CourtDTO.class);

        mockMvc.perform(
                        get(COURT_ENDPOINT + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(courtDTO)));
    }

    @Test
    void testGivenInvalidIdWhenGetCourtByIdThenReturnNull() throws Exception {
        mockMvc.perform(
                        get(COURT_ENDPOINT + "0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void testSaveNewCourt() throws Exception {
        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setAddress("Address2");
        CourtDTO savedCourtDTO = new CourtDTO(court.getId() + 2, "Address2", Set.of(), Set.of());
        mockMvc.perform(
                        post(COURT_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(courtDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(savedCourtDTO)));
        assertEquals(2L, repository.count());
    }

    @Test
    void testUpdateCourt() throws Exception {
        CourtDTO courtDTO = modelMapper.map(court, CourtDTO.class);
        courtDTO.setAddress("Address3");
        mockMvc.perform(
                        post(COURT_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(courtDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(courtDTO)));
        assertEquals(1L, repository.count());
    }

    @Test
    void testDeleteCourt() throws Exception {
        mockMvc.perform(
                        delete(COURT_ENDPOINT + court.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        assertEquals(0L, repository.count());
    }

    @Test
    void testGivenInvalidCourtIdWhenGetAllPricesForCourtThenNotFound() throws Exception {
        mockMvc.perform(
                        get(COURT_ENDPOINT + "0" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void testGivenValidCourtIdWhenGetAllPricesForCourtThenOk() throws Exception {
        mockMvc.perform(
                        get(COURT_ENDPOINT + court.getId() + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(court.getPrices())));
    }


    @Test
    void testGivenValidCourtIdWhenSetPriceThenReturnOk() throws Exception {
        PriceDTO priceDTO = new PriceDTO(1L, Season.WINTER, 20F, Boolean.TRUE, DayPeriod.MORNING);
        court.getPrices().stream().findFirst().ifPresent(price -> price.setValue(20F));
        CourtDTO updatedCourtDTO = modelMapper.map(court, CourtDTO.class);
        mockMvc.perform(
                        post(COURT_ENDPOINT + court.getId() + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(updatedCourtDTO)));
    }

    @Test
    void testGivenValidCourtIdWhenSetNewPriceThenReturnOk() throws Exception {
        Long nextPriceId = court.getPrices().iterator().next().getId() + 1;
        PriceDTO priceDTO = new PriceDTO(nextPriceId, Season.WINTER, 20F, Boolean.TRUE, DayPeriod.EVENING);
        court.getPrices().add(new Price(nextPriceId, Season.WINTER, 20F, Boolean.TRUE, DayPeriod.EVENING));
        CourtDTO updatedCourtDTO = modelMapper.map(court, CourtDTO.class);
        mockMvc.perform(
                        post(COURT_ENDPOINT + court.getId() + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(priceDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(updatedCourtDTO)));
    }

    @Test
    void testGivenInvalidCourtIdWhenSetPriceThenReturnNotFound() throws Exception {
        mockMvc.perform(
                        post(COURT_ENDPOINT + "0" + PRICE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(new PriceDTO())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void testGivenValidCourtIdAndValidPriceIdWhenDeletePriceThenReturnOk() throws Exception {
        Long priceId = court.getPrices().iterator().next().getId();
        mockMvc.perform(
                        delete(COURT_ENDPOINT + court.getId() + PRICE_ENDPOINT + "/" + priceId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Court fetchedCourt = repository.findById(court.getId()).orElseThrow();
        assertTrue(fetchedCourt.getPrices().isEmpty());
    }

    @Test
    void testGivenInvalidCourtIdWhenDeletePriceThenReturnNotFound() throws Exception {
        mockMvc.perform(
                        delete(COURT_ENDPOINT + "0" + PRICE_ENDPOINT + "/0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Court fetchedCourt = repository.findById(court.getId()).orElseThrow();
        assertEquals(1, fetchedCourt.getPrices().size());
    }

    @Test
    void testGivenValidCourtIdAndInvalidPriceIdWhenSetPriceThenReturnNotFound() throws Exception {
        mockMvc.perform(
                        delete(COURT_ENDPOINT + court.getId() + PRICE_ENDPOINT + "/0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        Court fetchedCourt = repository.findById(court.getId()).orElseThrow();
        assertEquals(1, fetchedCourt.getPrices().size());
    }
}
