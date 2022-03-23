package ro.courtreserve.model.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.courtreserve.model.DayPeriod;
import ro.courtreserve.model.Season;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceTest {
    private Price classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new Price();
        classUnderTest.setSeason(Season.SUMMER);
        classUnderTest.setWeekend(Boolean.TRUE);
        classUnderTest.setDayPeriod(DayPeriod.MORNING);
    }

    @Test
    void testNotEqualsPeriodDifferentSeason() {
        Price price = new Price();
        price.setSeason(Season.WINTER);

        assertFalse(classUnderTest.equalsPeriod(price));
    }

    @Test
    void testNotEqualsPeriodDifferentWeekend() {
        Price price = new Price();
        price.setSeason(Season.SUMMER);
        price.setWeekend(Boolean.FALSE);

        assertFalse(classUnderTest.equalsPeriod(price));
    }

    @Test
    void testNotEqualsPeriodDifferentDayPeriod() {
        Price price = new Price();
        price.setSeason(Season.SUMMER);
        price.setWeekend(Boolean.TRUE);
        price.setDayPeriod(DayPeriod.EVENING);

        assertFalse(classUnderTest.equalsPeriod(price));
    }

    @Test
    void testEqualsPeriod() {
        Price price = new Price();
        price.setSeason(Season.SUMMER);
        price.setWeekend(Boolean.TRUE);
        price.setDayPeriod(DayPeriod.MORNING);

        assertTrue(classUnderTest.equalsPeriod(price));
    }
}