package com.survey.vehicle.presenter;

import com.survey.vehicle.model.Vehicle;

import java.util.List;

public interface Presenter {
    void present(List<Vehicle> vehicles, boolean average);
}
