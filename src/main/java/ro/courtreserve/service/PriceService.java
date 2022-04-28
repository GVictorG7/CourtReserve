package ro.courtreserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.courtreserve.model.DayPeriod;
import ro.courtreserve.model.Season;
import ro.courtreserve.model.dto.CourtDTO;
import ro.courtreserve.model.dto.PriceDTO;
import ro.courtreserve.model.dto.ReservationDTO;
import ro.courtreserve.model.dto.SubscriptionDTO;

import java.util.Calendar;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final CourtService courtService;
    private final Calendar calendar = Calendar.getInstance();

    public Float calculateTotalPriceOfSubscription(SubscriptionDTO subscriptionDTO) {
        Season season = getSeasonBasedOnMonth(subscriptionDTO.getStartDateMonth());
        DayPeriod dayPeriod = getDatePeriodBasedOnHourInterval(subscriptionDTO.getStartHour());
        CourtDTO courtDTO = courtService.getCourtById(subscriptionDTO.getCourtId());
        Set<PriceDTO> priceDTOSet = courtDTO.getPrices();
        calendar.set(subscriptionDTO.getStartDateYear(), subscriptionDTO.getStartDateMonth() - 1, subscriptionDTO.getStartDateDay());
        int countWeekDays = 0;
        int countWeekendDays = 0;
        for (int day = subscriptionDTO.getStartDateDay(); day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek =  calendar.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                countWeekendDays++;
            } else {
                countWeekDays++;
            }
        }
        PriceDTO priceForWeekendDay = priceDTOSet.stream().filter(priceDTO -> priceDTO.equalsPeriod(season, Boolean.TRUE, dayPeriod)).findFirst().get();
        PriceDTO priceForWeekday = priceDTOSet.stream().filter(priceDTO -> priceDTO.equalsPeriod(season, Boolean.FALSE, dayPeriod)).findFirst().get();

        return priceForWeekday.getValue() * countWeekDays + priceForWeekendDay.getValue() * countWeekendDays;
    }

    public Float calculatePriceOfReservation(ReservationDTO reservationDTO) {
        Season season = getSeasonBasedOnMonth(reservationDTO.getMonth());
        DayPeriod dayPeriod = getDatePeriodBasedOnHourInterval(reservationDTO.getHour());
        CourtDTO courtDTO = courtService.getCourtById(reservationDTO.getCourtId());
        Set<PriceDTO> priceDTOSet = courtDTO.getPrices();
        calendar.set(reservationDTO.getYear(), reservationDTO.getMonth() - 1, reservationDTO.getDay());
        int dayOfWeek =  calendar.get(Calendar.DAY_OF_WEEK);

        Boolean isWeekend = Boolean.valueOf (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
        PriceDTO selectedPriceDTO = priceDTOSet.stream().filter(priceDTO -> priceDTO.equalsPeriod(season, isWeekend, dayPeriod)).findFirst().get();
        return selectedPriceDTO.getValue();
    }

    private Season getSeasonBasedOnMonth(Byte month) {
        if (3 <= month && month <= 5) {
            return Season.SPRING;
        }
        if (6 <= month && month <= 8) {
            return Season.SUMMER;
        }
        if (9 <= month && month <= 11) {
            return Season.AUTUMN;
        }
        return Season.WINTER;
    }

    private DayPeriod getDatePeriodBasedOnHourInterval(Byte startHour) {
        if (6 <= startHour && startHour < 12) {
            return DayPeriod.MORNING;
        }
        if (12 <= startHour && startHour < 18) {
            return DayPeriod.AFTERNOON;
        }
        if (18 <= startHour) {
            return DayPeriod.EVENING;
        }
        return DayPeriod.NIGHT;
    }

}
