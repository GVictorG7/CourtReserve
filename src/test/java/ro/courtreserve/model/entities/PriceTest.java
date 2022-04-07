package ro.courtreserve.model.entities;

import org.junit.jupiter.api.Test;
import ro.courtreserve.model.DayPeriod;
import ro.courtreserve.model.Season;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceTest {
    @Test
    void testOverrideFieldValues() {
        Price price1 = new Price();

        Price price2 = new Price(1L, Season.WINTER, 20F, Boolean.TRUE, DayPeriod.MORNING);

        price1.overrideFieldValues(price2);
        assertEquals(1L, price1.getId());
        assertEquals(Season.WINTER, price1.getSeason());
        assertEquals(20F, price1.getValue());
        assertTrue(price1.getWeekend());
        assertEquals(DayPeriod.MORNING, price1.getDayPeriod());
    }
}