package ro.courtreserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDTO {
    private Long fromUserId;
    private Long toUserId;
    private Long reservationId;
}
