package ro.courtreserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.courtreserve.model.dto.UserDTO;
import ro.courtreserve.service.UserService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String INVALID = "invalid";
    private static final String USER_LOGIN_ENDPOINT = "/user/login";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock
    private UserService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserController classUnderTest = new UserController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(classUnderTest).build();
    }

    @Test
    void testGivenValidCredentialsWhenLogInThenSuccess() throws Exception {
        // given
        UserDTO login = createUserDTO(true);

        UserDTO loginResponse = new UserDTO();
        Mockito.when(service.singIn(login)).thenReturn(loginResponse);

        mockMvc.perform(
                        post(USER_LOGIN_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string(MAPPER.writeValueAsString(loginResponse)));
        verify(service).singIn(login);
    }

    @Test
    void testGivenInvalidCredentialsWhenLogInThenFailure() throws Exception {
        // given
        UserDTO login = createUserDTO(false);

        Mockito.when(service.singIn(login)).thenReturn(null);

        mockMvc.perform(
                        post(USER_LOGIN_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(MAPPER.writeValueAsString(login)))
                .andExpect(status().isForbidden());
        verify(service).singIn(login);
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
