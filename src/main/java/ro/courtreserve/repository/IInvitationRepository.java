package ro.courtreserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.courtreserve.model.entities.Invitation;
import ro.courtreserve.model.entities.Reservation;
import ro.courtreserve.model.entities.User;

@Repository
public interface IInvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByFromAndToAndReservation(User from, User to, Reservation reservation);
}
