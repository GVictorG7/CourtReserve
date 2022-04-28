package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.model.entities.Reservation;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.IReservationRepository;
import ro.courtreserve.repository.IUserRepository;
import ro.courtreserve.util.MailService;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final IReservationRepository repository;
    private final IUserRepository userRepository;
    private final PriceService priceService;
    private final MailService mailService;
    private final ModelMapper mapper;

    public Float saveReservation(ReservationDTO rawReservationDTO) {
        Reservation reservation = mapper.map(rawReservationDTO, Reservation.class);
        User user = userRepository.findById(rawReservationDTO.getUserIds().iterator().next())
                .orElseThrow();
        reservation.getUsers().add(user);
        Reservation savedReservation = repository.save(reservation);
        ReservationDTO reservationDTO = mapper.map(savedReservation, ReservationDTO.class);
        Float price = priceService.calculatePriceOfReservation(reservationDTO);
        mailService.sendEmail(user.getMail(), reservationDTO, price);
        return price;
    }
}
