package com.survey.vehicle.parser;

public class DataParsingException extends Exception {

    public DataParsingException(String message) {
        super(message);
    }

    public DataParsingException(Exception e) {
        super(e);
    }
}
