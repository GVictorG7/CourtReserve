package ro.courtreserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.courtreserve.model.entities.Subscription;

@Repository
public interface ISubscriptionRepository extends JpaRepository<Subscription, Long> {
}
