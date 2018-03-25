package com.survey.vehicle.parser;

import com.survey.vehicle.model.Vehicle;

import java.util.List;
import java.util.stream.Stream;

public interface Parser {
    List<Vehicle> parse(String path) throws DataParsingException;
}
