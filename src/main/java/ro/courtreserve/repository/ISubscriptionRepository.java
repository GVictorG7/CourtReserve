package ro.courtreserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.model.entities.Subscription;

import java.util.List;

@Repository
public interface ISubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByCourtAndStartDateDayAndStartDateMonthAndStartDateYearAndStartHour(Court court, Byte startDateDay, Byte startDateMonth, Integer startDateYear, Byte startHour);
}
