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
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date_day")
    private Byte startDateDay;

    @Column(name = "start_date_month")
    private Byte startDateMonth;

    @Column(name = "start_date_year")
    private Integer startDateYear;

    @Column(name = "start_hour")
    private Byte startHour;

    @Column(name = "end_hour")
    private Byte endHour;

    @ManyToOne(fetch = FetchType.LAZY)
    private Court court;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable()
    private Set<User> users = new HashSet<>();
}
