package com.yiluhub.airflux.persisitence.converter;


import com.yiluhub.airflux.persistence.converter.DurationConverter;
import org.junit.Before;
import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

public class DurationConverterTest {

    DurationConverter durationConverter;

    @Before
    public void setup() {
       durationConverter = new DurationConverter();
    }

    @Test
    public void whenParseDurationWithDuration_thenReturnStringRepersentation() {
        Duration duration = Duration.ofHours(2).plusMinutes(30);
        String result = durationConverter.convertToDatabaseColumn(duration);

        assertEquals(result, "02:30");
    }

    @Test
    public void whenParseDurationWithString_thenReturnDuration() {
        Duration expectedResult = Duration.ofHours(2).plusMinutes(30);
        Duration result = durationConverter.convertToEntityAttribute("02:30");

        assertEquals(result, expectedResult);
    }
}
