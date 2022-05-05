package ro.courtreserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private Byte day;
    private Byte month;
    private Integer year;
    private Byte hour;
    private Long courtId;
    private Set<Long> userIds = new HashSet<>();
    private Set<InvitationDTO> invitations = new HashSet<>();

    public ReservationDTO(Byte day, Byte month, Integer year, Byte hour) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
    }
}
