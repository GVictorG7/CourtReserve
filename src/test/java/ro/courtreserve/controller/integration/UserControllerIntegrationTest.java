package ro.courtreserve.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.courtreserve.controller.UserController;
import ro.courtreserve.model.UserRole;
import ro.courtreserve.model.dto.UserDTO;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IUserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerIntegrationTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String INVALID = "invalid";
    private static final String MAIL = "admin@test.com";
    private static final String USER_LOGIN_ENDPOINT = "/user/login";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private UserController classUnderTest;
    @Autowired
    private IUserRepository repository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest).build();
        User user = new User(1L, USERNAME, PASSWORD, UserRole.ADMIN, MAIL);
        repository.save(user);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testGivenValidCredentialsWhenLogInThenSuccess() throws Exception {
        UserDTO login = createUserDTO(true);
        UserDTO loginResponse = new UserDTO(USERNAME, PASSWORD, UserRole.ADMIN, MAIL);

        mockMvc.perform(
                        post(USER_LOGIN_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(loginResponse)));
    }

    @Test
    void testGivenInvalidCredentialsWhenLogInThenFailure() throws Exception {
        UserDTO login = createUserDTO(false);

        mockMvc.perform(
                        post(USER_LOGIN_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(login)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));
    }

    private UserDTO createUserDTO(boolean valid) {
        UserDTO userDTO = new UserDTO();
        if (valid) {
            userDTO.setUsername(USERNAME);
            userDTO.setPassword(PASSWORD);
        } else {
            userDTO.setUsername(INVALID);
            userDTO.setPassword(INVALID);
        }
        return userDTO;
    }
}
