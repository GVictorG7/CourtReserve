package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.service.CourtService;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/court")
@RequiredArgsConstructor
public class CourtController {
    private final CourtService service;

    @GetMapping("/")
    public ResponseEntity<List<CourtDTO>> getCourts() {
        List<CourtDTO> courts = service.getAllCourts();
        return ResponseEntity.ok(courts);
    }

    @PostMapping("/")
    public ResponseEntity<CourtDTO> saveCourt(@RequestBody CourtDTO courtDTO) {
        CourtDTO savedCourt = service.saveCourt(courtDTO);
        return ResponseEntity.ok(savedCourt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        service.deleteCourt(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<CourtDTO> setPrice(@PathVariable Long id, @RequestBody PriceDTO priceDTO) {
        CourtDTO savedCourt = service.setPriceForCourt(id, priceDTO);
        HttpStatus status = savedCourt == null ? NOT_FOUND : OK;
        return ResponseEntity.status(status).body(savedCourt);
    }

    @DeleteMapping("/{id}/price")
    public ResponseEntity<CourtDTO> deletePrice(@PathVariable Long id, @RequestBody PriceDTO priceDTO) {
        CourtDTO savedCourt = service.deletePriceOfCourt(id, priceDTO);
        HttpStatus status = savedCourt == null ? NOT_FOUND : OK;
        return ResponseEntity.status(status).body(savedCourt);
    }
}
