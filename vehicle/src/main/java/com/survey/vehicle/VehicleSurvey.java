package com.survey.vehicle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.survey.vehicle.model.Vehicle;
import com.survey.vehicle.parser.Parser;
import com.survey.vehicle.presenter.CarDistancePresenter;
import com.survey.vehicle.presenter.PeakVolumeTimesPresenter;
import com.survey.vehicle.presenter.Presenter;
import com.survey.vehicle.presenter.SpeedDistributionPresenter;
import com.survey.vehicle.presenter.VehicleCountPresenter;

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
        Collection<Presenter> localPresenters = new LinkedList<>();
        localPresenters.add(new VehicleCountPresenter());
        localPresenters.add(new SpeedDistributionPresenter());
        localPresenters.add(new PeakVolumeTimesPresenter());
        localPresenters.add(new CarDistancePresenter());
        return localPresenters;
    }

}
