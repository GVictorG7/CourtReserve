package ro.courtreserve.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;

@Entity
@Table(name = "invitations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invitation implements Serializable {
    public Invitation(User from, User to, Reservation reservation) {
        this.from = from;
        this.to = to;
        this.reservation = reservation;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private User from;

    @ManyToOne
    private User to;

    @ManyToOne
    private Reservation reservation;
}
