package com.survey.vehicle.handler;

import com.survey.vehicle.model.Direction;
import com.survey.vehicle.model.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class SpeedDistributionPresenter extends AbstractPresenter {

    public void showPerPeriod(Map<Integer, List<Vehicle>> vehiclesPerDay, Function<? super Vehicle, Boolean> filter,
                              Function<? super Vehicle, Integer> groupBy) {
        for (Integer key : vehiclesPerDay.keySet()) {
            getOut().println("day: " + getDayStr(key));
            List<Vehicle> vehicles = vehiclesPerDay.get(key);
            Map<Integer, Double> vehiclesPerPeriod = vehicles.stream().filter(v -> filter.apply(v))
                    .collect(Collectors.groupingBy(groupBy, Collectors.averagingDouble(
                            v -> {

                                long x = v.getBackAxleTime() - v.getFrontAxleTime();
                                double y = ((2.5 / x) * oneHourMili) / 1000;
                                return y;

                            }
                    )));

            vehiclesPerPeriod.forEach((k, v) -> getOut().println("period: " + k + "\t speed: " + v));
        }
    }


    public void showAveragePerPeriod(List<Vehicle> vehicles, Function<? super Vehicle, Boolean> filter,
                                     Function<? super Vehicle, Integer> groupBy) {
        Map<Integer, Map<Integer, Double>> map = vehicles.stream().filter(v -> filter.apply(v)).collect(Collectors
                .groupingBy(groupBy, Collectors.groupingBy(Vehicle::getDay, Collectors.averagingDouble(
                        v -> {

                            long x = v.getBackAxleTime() - v.getFrontAxleTime();
                            double y = ((2.5 / x) * oneHourMili) / 1000;
                            return y;

                        }
                ))));

        int size = map.keySet().stream().reduce(Integer::max).orElse(0);
        int days = 5;
        for (int i = 1; i <= size; i++) {
            double count = 0;
            for (int j = 0; j < days; j++) {
                if (map.containsKey(i) && map.get(i).containsKey(j)) {
                    count += map.get(i).get(j);
                }
            }
            double avg = count / days;
            getOut().println("period: " + i + "\t count: " + avg);
        }
    }


    @Override
    public void present(List<Vehicle> vehicles, boolean average) {
        if (vehicles == null) {
            getOut().println(" No cars went to this direction ");
        } else {
            Direction[] directions = new Direction[]{
                    Direction.NORTH_BOUND, Direction.SOUTH_BOUND
            };
            for (Direction direction : directions) {
                getOut().println(direction);

                Map<Integer, List<Vehicle>> vehiclesPerDay = vehicles.stream()
                        .filter(vehicle -> direction
                                .equals(vehicle.getDirection())).collect(Collectors
                                .groupingBy(Vehicle::getDay));

                if (vehiclesPerDay.isEmpty()) {
                    getOut().println(" No cars went to this direction ");
                } else {
                    if (!average) {
                        showPerPeriod(vehiclesPerDay, v -> v.getTwelveHourGroup() != 0, Vehicle::getTwelveHourGroup);
                        showPerPeriod(vehiclesPerDay, v -> v.getOneHourGroup() != 0, Vehicle::getOneHourGroup);
                        showPerPeriod(vehiclesPerDay, v -> v.getThirtyMinsGroup() != 0, Vehicle::getThirtyMinsGroup);
                        showPerPeriod(vehiclesPerDay, v -> v.getTwentyMinsGroup() != 0, Vehicle::getTwentyMinsGroup);
                        showPerPeriod(vehiclesPerDay, v -> v.getFifteenMinsGroup() != 0, Vehicle::getFifteenMinsGroup);
                    } else {
                        List<Vehicle> vehiclesPerDirection = vehicles.stream()
                                .filter(vehicle -> direction.equals(vehicle.getDirection()))
                                .collect(Collectors.toList());

                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getTwelveHourGroup() != 0,
                                Vehicle::getTwelveHourGroup);
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getOneHourGroup() != 0,
                                Vehicle::getOneHourGroup);
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getThirtyMinsGroup() != 0,
                                Vehicle::getThirtyMinsGroup);
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getTwentyMinsGroup() != 0,
                                Vehicle::getTwentyMinsGroup);
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getFifteenMinsGroup() != 0,
                                Vehicle::getFifteenMinsGroup);
                    }
                }

            }
        }
    }
}
