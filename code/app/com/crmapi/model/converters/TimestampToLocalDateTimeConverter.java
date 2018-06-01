package com.crmapi.model.converters;

import org.jooq.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimestampToLocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {

    @Override
    public LocalDateTime from(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    @Override
    public Timestamp to(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<LocalDateTime> toType() {
        return LocalDateTime.class;
    }
}
