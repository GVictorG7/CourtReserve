package ro.courtreserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private Byte startDateDay;
    private Byte startDateMonth;
    private Integer startDateYear;
    private Byte startHour;
    private Byte endHour;
    private Long courtId;
    private Set<Long> userIds = new HashSet<>();
}
