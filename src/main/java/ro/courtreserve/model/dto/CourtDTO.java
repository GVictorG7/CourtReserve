package ro.courtreserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourtDTO {
    private Long id;
    private String address;
    private Set<PriceDTO> prices = new HashSet<>();
    private Set<Long> reservationIds = new HashSet<>();
    private Set<Long> subscriptionIds = new HashSet<>();


    public CourtDTO(Long id, String address) {
        this.id = id;
        this.address = address;
    }
}
