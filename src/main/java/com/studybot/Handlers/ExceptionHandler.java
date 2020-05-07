package com.studybot.Handlers;

import org.apache.log4j.Logger;

public final class ExceptionHandler {

    private static Logger log = Logger.getLogger(ExceptionHandler.class.getName());

    public static void handleException(Exception e) {
        log.error(e.getMessage(), e);
    }
}
