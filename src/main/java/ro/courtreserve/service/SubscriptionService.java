package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.dto.SubscriptionDTO;
import ro.courtreserve.model.entities.Subscription;
import ro.courtreserve.model.entities.User;
import ro.courtreserve.repository.ISubscriptionRepository;
import ro.courtreserve.repository.IUserRepository;
import ro.courtreserve.util.MailService;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final ISubscriptionRepository subscriptionRepository;
    private final IUserRepository userRepository;
    private final PriceService priceService;
    private final MailService mailService;
    private final ModelMapper mapper;

    /**
     * month, court, time interval
     */
    public Float saveSubscription(SubscriptionDTO rawSubscriptionDTO) {
        Subscription subscription = mapper.map(rawSubscriptionDTO, Subscription.class);
        User user = userRepository.findById(rawSubscriptionDTO.getUserIds().iterator().next()).orElseThrow();
        subscription.getUsers().add(user);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        SubscriptionDTO savedSubscriptionDTO = mapper.map(savedSubscription, SubscriptionDTO.class);
        Float price = priceService.calculateTotalPriceOfSubscription(savedSubscriptionDTO);
        mailService.sendEmail(user.getMail(), savedSubscriptionDTO, price);
        return price;
    }
}