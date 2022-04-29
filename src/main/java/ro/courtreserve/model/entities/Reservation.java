package ro.courtreserve.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "day")
    private Byte day;

    @Column(name = "month")
    private Byte month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "hour")
    private Byte hour;

    @ManyToOne(fetch = FetchType.LAZY)
    private Court court;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable()
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER)
    private Set<Invitation> invitationsFor = new HashSet<>();
}
