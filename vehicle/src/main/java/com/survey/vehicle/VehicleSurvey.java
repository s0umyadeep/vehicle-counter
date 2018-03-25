package com.survey.vehicle;

import com.survey.vehicle.handler.*;
import com.survey.vehicle.model.Vehicle;
import com.survey.vehicle.parser.DataParser;
import com.survey.vehicle.parser.Parser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class VehicleSurvey {

    private Collection<Presenter> presenters;
    private boolean average;
    private Parser parser;

    public boolean isAverage() {
        return average;
    }

    public void setAverage(boolean average) {
        this.average = average;
    }

    public void setPresenters(Collection<Presenter> presenters) {
        this.presenters = presenters;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    private void notifyPresenters(List<Vehicle> vehicles) {
        for (Presenter presenter : presenters) {
            presenter.present(vehicles, false);
        }
    }

    public void process(String path) throws Exception {
        Parser parser = getParser();
        List<Vehicle> vehicles = parser.parse(path);
        notifyPresenters(vehicles);
    }

    public Collection<Presenter> configurePresenters() {
        Collection<Presenter> analysers = new LinkedList<>();
        analysers.add(new VehicleCountPresenter());
        analysers.add(new SpeedDistributionPresenter());
        analysers.add(new PeakVolumeTimesPresenter());
        analysers.add(new CarDistancePresenter());
        return analysers;
    }

}
