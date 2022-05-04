package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.service.ReservationService;
import ro.courtreserve.util.exception.CourtUnavailableException;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService service;

    @PostMapping("/")
    public ResponseEntity<Float> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            Float price = service.saveReservation(reservationDTO);
            return ResponseEntity.ok(price);
        } catch (CourtUnavailableException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
