package ro.courtreserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.model.entities.Reservation;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByCourtAndDayAndMonthAndYearAndHour(Court court, Byte day, Byte month, Integer year, Byte hour);
}
