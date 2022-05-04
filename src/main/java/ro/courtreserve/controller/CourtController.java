package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.service.CourtService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/court")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CourtController {
    private final CourtService service;

    /**
     * Retrieves all the {@link CourtDTO}
     *
     * @return a {@link List} with all the {@link CourtDTO} objects
     */
    @GetMapping("/")
    public ResponseEntity<List<CourtDTO>> getCourts() {
        List<CourtDTO> courts = service.getAllCourts();
        return ResponseEntity.ok(courts);
    }

    /**
     * Retrieves the {@link CourtDTO} corresponding to the given id as PathVariable
     *
     * @param id the id of the {@link CourtDTO}
     * @return status code 200 with the {@link CourtDTO} with the corresponding id or status code 404 if the provided id
     * was not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourtDTO> getCourtById(@PathVariable Long id) {
        try {
            CourtDTO courtDTO = service.getCourtById(id);
            return ResponseEntity.ok(courtDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Saves a new or updates an existing {@link CourtDTO}. For saving a new {@link CourtDTO} provide only the address
     * field. In order to correctly modify an existing {@link CourtDTO}, the exact list of {@link PriceDTO}s must be
     * provided
     *
     * @param courtDTO the {@link CourtDTO} object to be persisted of modified
     * @return status code 200 with the {@link CourtDTO} object as it was persisted
     */
    @PostMapping("/")
    public ResponseEntity<CourtDTO> saveCourt(@RequestBody CourtDTO courtDTO) {
        CourtDTO savedCourt = service.saveCourt(courtDTO);
        return ResponseEntity.ok(savedCourt);
    }

    /**
     * Deletes a {@link CourtDTO} based on the given id as PathVariable
     *
     * @param id the id of the {@link CourtDTO} to be deleted
     * @return status code 200
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        service.deleteCourt(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> isCourtAvailable(@PathVariable Long id, @RequestBody ReservationDTO reservation) {
        Boolean isAvailable = service.isCourtAvailable(id, reservation.getDay(), reservation.getMonth(), reservation.getYear(), reservation.getHour());
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Retrieves as set with the {@link PriceDTO}s of the {@link CourtDTO} with the given id as PathVariable
     *
     * @param id the id of the {@link CourtDTO}
     * @return status code 200 with the set of {@link PriceDTO}s or status code 404 if no {@link CourtDTO} exists with
     * the given id
     */
    @GetMapping("/{id}/price")
    public ResponseEntity<Set<PriceDTO>> getAllPricesForCourt(@PathVariable Long id) {
        try {
            Set<PriceDTO> prices = service.getAllPricesForCourt(id);
            return ResponseEntity.ok(prices);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves the {@link PriceDTO} with the given id as PathVariable, corresponding to the {@link CourtDTO} with the
     * given id as PathVariable
     *
     * @param courtId the id of the {@link CourtDTO}
     * @param priceId the id of the {@link PriceDTO}
     * @return status code 200 with the searched {@link PriceDTO} or status code 404 if the {@link CourtDTO} or
     * {@link PriceDTO} with the given ids were not found
     */
    @GetMapping("/{courtId}/price/{priceId}")
    public ResponseEntity<PriceDTO> getPriceById(@PathVariable Long courtId, @PathVariable Long priceId) {
        try {
            PriceDTO priceDTO = service.getPriceById(courtId, priceId);
            return ResponseEntity.ok(priceDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Sets a {@link PriceDTO} for the {@link CourtDTO} with the given id provided by PathVariable. If there is no other
     * {@link PriceDTO} with the same time period of time for the {@link CourtDTO} then the price will be inserted as
     * new, otherwise only its value will be updated
     *
     * @param id       the id of the {@link CourtDTO}
     * @param priceDTO the {@link PriceDTO} to be added of modified
     * @return status code 200 with the {@link CourtDTO} object as it was persisted with the new or modified
     * {@link PriceDTO} or status code 404 if the provided id does not correspond to any existing {@link CourtDTO}
     */
    @PostMapping("/{id}/price")
    public ResponseEntity<CourtDTO> setPrice(@PathVariable Long id, @RequestBody PriceDTO priceDTO) {
        try {
            CourtDTO savedCourt = service.setPriceForCourt(id, priceDTO);
            return ResponseEntity.ok(savedCourt);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a {@link PriceDTO} for the {@link CourtDTO} with the given id provided by PathVariable. The
     * {@link PriceDTO} is identified based on its id provided as PathVariable
     *
     * @param courtId the id of the {@link CourtDTO}
     * @param priceId the id of the {@link PriceDTO} to be deleted
     * @return status code 200 with the {@link CourtDTO} object as it was persisted with the deleted {@link PriceDTO} or
     * status code 404 if the provided id does not correspond to any existing {@link CourtDTO}
     */
    @DeleteMapping("/{courtId}/price/{priceId}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long courtId, @PathVariable Long priceId) {
        HttpStatus status = OK;
        try {
            service.deletePriceOfCourt(courtId, priceId);
        } catch (NoSuchElementException e) {
            status = NOT_FOUND;
        }
        return ResponseEntity.status(status).build();
    }
}
