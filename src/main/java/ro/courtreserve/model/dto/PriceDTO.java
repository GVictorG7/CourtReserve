package ro.courtreserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.courtreserve.model.DayPeriod;
import ro.courtreserve.model.Season;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDTO {
    private Season season;
    private Float value;
    private Boolean weekend;
    private DayPeriod dayPeriod;
}
