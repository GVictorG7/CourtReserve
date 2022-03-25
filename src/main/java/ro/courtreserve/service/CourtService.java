package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.model.entities.Court;
import ro.courtreserve.model.entities.Price;
import ro.courtreserve.repository.ICourtRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourtService {
    private final ICourtRepository courtRepository;
    private final ModelMapper mapper;

    /**
     * Retrieves all the {@link CourtDTO} objects
     *
     * @return a {@link List} with all the {@link CourtDTO} objects
     */
    public List<CourtDTO> getAllCourts() {
        List<Court> courts = courtRepository.findAll();
        return courts.stream().map(courtDTO -> mapper.map(courtDTO, CourtDTO.class)).collect(Collectors.toList());
    }

    /**
     * Persists a given {@link CourtDTO} object, or modifies an existing one, based on the courtId.
     * NOTE: in order to correctly modify an existing {@link Court}, the exact list of {@link Price}s must be provided
     *
     * @param courtDTO the {@link CourtDTO} object to be persisted of modified
     * @return the {@link CourtDTO} object as it was persisted
     */
    public CourtDTO saveCourt(CourtDTO courtDTO) {
        Court court = mapper.map(courtDTO, Court.class);
        if (court.getPrices() == null) {
            court.setPrices(new ArrayList<>());
        }
        Court savedCourt = courtRepository.save(court);
        return mapper.map(savedCourt, CourtDTO.class);
    }

    /**
     * Deletes a {@link Court} based on its id
     *
     * @param id the id of the {@link Court} to be deleted
     */
    public void deleteCourt(Long id) {
        courtRepository.deleteById(id);
    }

    /**
     * Sets a {@link PriceDTO} for the {@link CourtDTO} with the given id. If there is no other {@link PriceDTO} with
     * the same time period of time for the {@link CourtDTO} then the price will be inserted as new, otherwise only its
     * value will be updated
     *
     * @param courtId  the id of the {@link CourtDTO}
     * @param priceDTO the {@link PriceDTO} to be added of modified
     * @return the {@link CourtDTO} that was updated with the given priceDTO, or null if the provided courtId does not
     * correspond to an existing {@link Court}
     */
    public CourtDTO setPriceForCourt(Long courtId, PriceDTO priceDTO) {
        Court court = courtRepository.findById(courtId).orElse(null);
        if (court != null) {
            Price price = mapper.map(priceDTO, Price.class);

            setCourtPrice(court, price);

            Court savedCourt = courtRepository.save(court);
            return mapper.map(savedCourt, CourtDTO.class);
        } else {
            return null;
        }
    }

    /**
     * Deletes a {@link PriceDTO} for the {@link CourtDTO} with the given id. The {@link PriceDTO} is identified based
     * on its time period
     *
     * @param courtId  the id of the {@link CourtDTO}
     * @param priceDTO the {@link PriceDTO} to be deleted
     * @return the {@link CourtDTO} that was updated with the deleted priceDTO, or null if the provided courtId does not
     * correspond to an existing {@link Court}
     */
    public CourtDTO deletePriceOfCourt(Long courtId, PriceDTO priceDTO) {
        Court court = courtRepository.findById(courtId).orElse(null);
        if (court != null) {
            Price price = mapper.map(priceDTO, Price.class);

            court.getPrices().removeIf(p -> p.equalsPeriod(price));

            Court savedCourt = courtRepository.save(court);
            return mapper.map(savedCourt, CourtDTO.class);
        } else {
            return null;
        }
    }

    /**
     * For the given {@link Court}, updates its existing price with the value of the newPrice received as parameter if
     * there is an existing price with the same time period, of inserts it as new otherwise
     *
     * @param court    the {@link Court} whose price will be updated or added
     * @param newPrice the {@link Price} to be updated or added
     */
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
