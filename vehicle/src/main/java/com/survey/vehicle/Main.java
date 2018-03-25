package com.survey.vehicle;


import com.survey.vehicle.handler.Presenter;
import com.survey.vehicle.parser.DataParser;

import java.io.File;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        VehicleSurvey vehicleSurvey = new VehicleSurvey();
        Collection<Presenter> presenters = vehicleSurvey.configurePresenters();
        vehicleSurvey.setPresenters(presenters);
        vehicleSurvey.setAverage(true);
        vehicleSurvey.setParser(new DataParser());

        File file = new File(Main.class.getClassLoader().getResource("abc.txt").getFile());
        vehicleSurvey.process(file.getAbsolutePath());
    }

}
