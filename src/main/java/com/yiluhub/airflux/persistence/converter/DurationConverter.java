package com.yiluhub.airflux.persistence.converter;

import com.yiluhub.airflux.exception.ApplicationException;
import com.yiluhub.airflux.logging.AirLogger;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;


@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, String>, AirLogger {

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        getLogger().info(String.format("Converting %s to String", duration));
        return parseDuration(duration);
    }

    @Override
    public Duration convertToEntityAttribute(String duration) {
        getLogger().info(String.format("Converting %s to Duration", duration));
        return parseDuration(duration);
    }

    private Duration parseDuration(String periodDuration) {

        try{
            String[] durationArr = periodDuration.split(":");
            Long hours = Long.parseLong(durationArr[0]);
            Long minutes = Long.parseLong(durationArr[1]);
            Duration duration = Duration.ofHours(hours).plusMinutes(minutes);

            return duration;
        } catch (RuntimeException e) {

            getLogger().error("Error parsing duration", e);
            throw new ApplicationException(e.getMessage());
        }
    }

    private String parseDuration(Duration duration) {
        Long hours = duration.toHours();
        Long minutes = duration.toMinutes() - hours * 60;

        return String.format("%02d:%02d", hours, minutes);
    }
}

