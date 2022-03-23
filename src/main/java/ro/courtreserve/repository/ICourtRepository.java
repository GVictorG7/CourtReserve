package ro.courtreserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.courtreserve.model.entities.Court;

@Repository
public interface ICourtRepository extends JpaRepository<Court, Long> {
}
