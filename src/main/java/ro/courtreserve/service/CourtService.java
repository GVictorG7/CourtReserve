package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.model.entities.Price;
import ro.courtreserve.repository.ICourtRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourtService {
    private final ICourtRepository courtRepository;
    private final ModelMapper mapper;

    public List<CourtDTO> getAllCourts() {
        List<Court> courts = courtRepository.findAll();
        return courts.stream().map(courtDTO -> mapper.map(courtDTO, CourtDTO.class)).collect(Collectors.toList());
    }

    public void createCourt(CourtDTO courtDTO) {
        Court court = mapper.map(courtDTO, Court.class);
        courtRepository.save(court);
    }

    public void deleteCourt(Long id) {
        courtRepository.deleteById(id);
    }

    public boolean updateCourtDetails(CourtDTO newCourtDTO) {
        Optional<Court> optionalCourt = courtRepository.findById(newCourtDTO.getId());
        if (optionalCourt.isPresent()) {
            Court court = optionalCourt.get();
            court.setAddress(newCourtDTO.getAddress());
            court.setAvailable(newCourtDTO.getAvailable());
            courtRepository.save(court);
            return true;
        } else {
            return false;
        }
    }

    public boolean setPriceForCourt(Long courtId, PriceDTO priceDTO) {
        Optional<Court> optionalCourt = courtRepository.findById(courtId);
        if (optionalCourt.isPresent()) {
            Court court = optionalCourt.get();
            Price price = mapper.map(priceDTO, Price.class);

            setCourtPrice(court, price);

            courtRepository.save(court);
            return true;
        } else {
            return false;
        }
    }

    public boolean deletePriceOfCourt(Long courtId, PriceDTO priceDTO) {
        Optional<Court> optionalCourt = courtRepository.findById(courtId);
        if (optionalCourt.isPresent()) {
            Court court = optionalCourt.get();
            Price price = mapper.map(priceDTO, Price.class);

            Optional<Price> existingPrice = court.getPrices().stream().filter(p -> p.equalsPeriod(price)).findFirst();
            existingPrice.ifPresent(value -> court.getPrices().remove(value));

            courtRepository.save(court);
            return true;
        } else {
            return false;
        }
    }

    private void setCourtPrice(Court court, Price newPrice) {
        Optional<Price> existingPrice = court.getPrices().stream().filter(p -> p.equalsPeriod(newPrice)).findFirst();
        if (existingPrice.isPresent()) {
            Price price = existingPrice.get();
            price.setValue(newPrice.getValue());
        } else {
            court.getPrices().add(newPrice);
        }
    }
}
