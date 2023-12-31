package ro.courtreserve.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import ro.courtreserve.model.DayPeriod;
import ro.courtreserve.model.Season;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "prices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Price implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", length = 40, nullable = false)
    private Season season;

    @Column(name = "value", nullable = false)
    private Float value;

    @Column(name = "weekend", nullable = false)
    private Boolean weekend;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_period", nullable = false)
    private DayPeriod dayPeriod;

    /**
     * Overrides the value of each field with the given {@link Price}
     *
     * @param price the {@link Price} whose values will be used
     */
    public void overrideFieldValues(Price price) {
        id = price.getId();
        season = price.getSeason();
        value = price.getValue();
        weekend = price.getWeekend();
        dayPeriod = price.getDayPeriod();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Price price = (Price) o;
        return id != null && Objects.equals(id, price.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
