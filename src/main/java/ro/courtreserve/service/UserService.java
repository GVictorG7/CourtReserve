package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.dto.UserDTO;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IUserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository repository;
    private final ModelMapper mapper;

    public UserDTO singIn(UserDTO userDTO) {
        User signedInUser = repository.getByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        if (signedInUser == null) {
            return null;
        }
        return mapper.map(signedInUser, UserDTO.class);
    }
}
