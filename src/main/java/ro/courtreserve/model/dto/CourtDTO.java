package ro.courtreserve.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourtDTO {
    private Long id;
    private String address;
    private Boolean available;
    private List<PriceDTO> prices;
}
