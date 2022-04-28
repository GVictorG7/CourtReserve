package ro.courtreserve.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.courtreserve.model.dto.SubscriptionDTO;
import ro.courtreserve.service.SubscriptionService;

@Controller
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService service;

    @PostMapping("/")
    public ResponseEntity<Float> saveSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Float totalPrice = service.saveSubscription(subscriptionDTO);
        return ResponseEntity.ok(totalPrice);
    }
}
