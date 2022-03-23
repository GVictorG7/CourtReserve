package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.service.CourtService;

import java.util.List;

@Controller
@RequestMapping("/court")
@RequiredArgsConstructor
public class CourtController {
    private final CourtService service;

    @GetMapping("/")
    public ResponseEntity<List<CourtDTO>> getCourts() {
        List<CourtDTO> courts = service.getAllCourts();
        return new ResponseEntity<>(courts, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createCourt(@RequestBody CourtDTO courtDTO) {
        service.createCourt(courtDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        service.deleteCourt(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateCourt(@RequestBody CourtDTO courtDTO) {
        boolean success = service.updateCourtDetails(courtDTO);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<Void> setPrice(@PathVariable Long id, @RequestBody PriceDTO priceDTO) {
        boolean success = service.setPriceForCourt(id, priceDTO);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/price")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id, @RequestBody PriceDTO priceDTO) {
        boolean success = service.deletePriceOfCourt(id, priceDTO);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
