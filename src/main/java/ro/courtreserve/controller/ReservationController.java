package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.service.ReservationService;
import ro.courtreserve.util.MailService;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService service;
    private final MailService mailService;

    @PostMapping("/")
    public ResponseEntity<Float> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        Float price = service.saveReservation(reservationDTO);
        return ResponseEntity.ok(price);
    }
}
