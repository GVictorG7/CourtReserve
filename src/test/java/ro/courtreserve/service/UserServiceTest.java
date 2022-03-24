package ro.courtreserve.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ro.courtreserve.model.UserRole;
import ro.courtreserve.model.dto.UserDTO;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IUserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL = "admin@test.com";
    private static final String INVALID = "invalid";

    private UserService classUnderTest;

    @Mock
    private IUserRepository repository;
    @Mock
    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classUnderTest = new UserService(repository, mapper);
    }

    @Test
    void singInSuccessful() {
        // given
        UserDTO dto = new UserDTO();
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);

        UserDTO signedInUserDTO = new UserDTO(USERNAME, PASSWORD, UserRole.ADMIN, MAIL);

        when(repository.getByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(new User());
        when(mapper.map(any(User.class), eq(UserDTO.class))).thenReturn(signedInUserDTO);

        // when
        UserDTO actualResult = classUnderTest.singIn(dto);

        // then
        assertEquals(signedInUserDTO, actualResult);
    }

    @Test
    void singInFailed() {
        // given
        UserDTO dto = new UserDTO();
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);

        when(repository.getByUsernameAndPassword(INVALID, INVALID)).thenReturn(null);

        // when
        UserDTO actualResult = classUnderTest.singIn(dto);

        // then
        assertNull(actualResult);
    }
}