package ro.courtreserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.courtreserve.model.entities.Reservation;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
}
