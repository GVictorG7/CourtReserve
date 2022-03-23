package ro.courtreserve.model.dto;

import lombok.Data;
import ro.courtreserve.model.Season;

@Data
public class PriceDTO {
    private Season season;
    private Float value;
    private Boolean weekend;
    private Boolean night;
}
