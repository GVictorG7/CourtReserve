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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Court implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "address", length = 40, nullable = false)
    private String address;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id")
    private Set<Price> prices = new HashSet<>();

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Subscription> subscriptions = new HashSet<>();

    public Court(Long id, String address) {
        this.id = id;
        this.address = address;
    }
}
