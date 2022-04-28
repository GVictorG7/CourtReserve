package ro.courtreserve.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.courtreserve.model.UserRole;
import ro.courtreserve.model.entities.User;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class IUserRepositoryTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL = "admin@test.com";
    private static final String INVALID = "invalid";

    @Autowired
    private IUserRepository classUnderTest;

    @BeforeEach
    void setUp() {
        User user = new User(1L, USERNAME, PASSWORD, UserRole.ADMIN, MAIL, Set.of());
        classUnderTest.save(user);
    }

    @AfterEach
    void tearDown() {
        classUnderTest.deleteAll();
    }

    @Test
    void testFindByUsernameAndPasswordRetrievesResult() {
        User fetchedUser = classUnderTest.getByUsernameAndPassword(USERNAME, PASSWORD);
        assertNotNull(fetchedUser);
    }

    @Test
    void testFindByUsernameAndPasswordDoesNotRetrievesResult() {
        User fetchedUser = classUnderTest.getByUsernameAndPassword(INVALID, INVALID);
        assertNull(fetchedUser);
    }
}
