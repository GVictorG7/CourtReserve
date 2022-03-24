package ro.courtreserve.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.courtreserve.model.UserRole;
import ro.courtreserve.model.entities.User;

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
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setMail(MAIL);
        user.setRole(UserRole.ADMIN);
        classUnderTest.save(user);
    }

    @AfterEach
    void tearDown() {
        classUnderTest.deleteAll();
    }

    @Test
    void testFindByUsernameAndPasswordRetrievesResult() {
        User fetchedUser = classUnderTest.findByUsernameAndPassword(USERNAME, PASSWORD);
        assertNotNull(fetchedUser);
    }

    @Test
    void testFindByUsernameAndPasswordDoesNotRetrievesResult() {
        User fetchedUser = classUnderTest.findByUsernameAndPassword(INVALID, INVALID);
        assertNull(fetchedUser);
    }
}
