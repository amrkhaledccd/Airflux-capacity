package com.yiluhub.airflux.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AirLogger {

    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
