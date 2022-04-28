package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.dto.UserDTO;
import ro.courtreserve.model.entities.Reservation;
import ro.courtreserve.model.entities.Subscription;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IUserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository repository;
    private final ModelMapper mapper;

    /**
     * Retrieves the existing {@link UserDTO} with the given username and password
     *
     * @param userDTO the {@link UserDTO} with the username and password to search
     * @return the {@link UserDTO} object with all the details or null if there is no {@link User} with the given
     * credentials in the database
     */
    public UserDTO singIn(UserDTO userDTO) {
        User signedInUser = repository.getByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        if (signedInUser == null) {
            return null;
        }
        UserDTO signedInUserDTO = mapper.map(signedInUser, UserDTO.class);
        Set<Long> reservationIds = signedInUser.getReservations().stream().map(Reservation::getId).collect(Collectors.toSet());
        signedInUserDTO.getReservationIds().addAll(reservationIds);
        Set<Long> subscriptionIds = signedInUser.getSubscriptions().stream().map(Subscription::getId).collect(Collectors.toSet());
        signedInUserDTO.getSubscriptionIds().addAll(subscriptionIds);
        return signedInUserDTO;
    }
}
