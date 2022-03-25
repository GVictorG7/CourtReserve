package ro.courtreserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.courtreserve.model.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves from the database the {@link User} with the given username and password
     *
     * @param username the username of the {@link User}
     * @param password the password of the {@link User}
     * @return the {@link User} persistent object with the given username and password or null if there is no
     * corresponding object in the database
     */
    User getByUsernameAndPassword(String username, String password);
}
