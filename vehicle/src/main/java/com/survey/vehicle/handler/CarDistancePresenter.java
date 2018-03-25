package com.survey.vehicle.handler;

import com.survey.vehicle.model.Direction;
import com.survey.vehicle.model.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CarDistancePresenter extends AbstractPresenter {


    public void showPerPeriod(Map<Integer, List<Vehicle>> vehiclesPerDay, Function<? super Vehicle, Boolean> filter,
                              Function<? super Vehicle, Integer> groupBy, String description) {

        for (Integer key : vehiclesPerDay.keySet()) {
            getOut().println("day: " + getDayStr(key));
            getOut().println("Interval : " + description);
            List<Vehicle> vehicles = vehiclesPerDay.get(key);
            Map<Integer, List<Vehicle>> vehiclesPerPeriod = vehicles.stream().filter(v -> filter.apply(v))
                    .collect(Collectors.groupingBy(groupBy));

            Map<Integer, Double> avgSpeedPerPeriod = new HashMap<>();
            vehiclesPerPeriod.forEach((k, v) -> {
                avgSpeedPerPeriod.put(k, distance(v));
            });

            avgSpeedPerPeriod.forEach((k, v) -> {
                getOut().println("period = " + k + ", distance = " + v);
            });

        }
    }

    public void showAveragePerPeriod(List<Vehicle> vehicles, Function<? super Vehicle, Boolean> filter,
                                     Function<? super Vehicle, Integer> groupBy, String description) {

        getOut().println("Interval : " + description);
        Map<Integer, Map<Integer, List<Vehicle>>> map = vehicles.stream().filter(v -> filter.apply(v))
                .collect(Collectors.groupingBy(groupBy, Collectors.groupingBy(Vehicle::getDay)));

        int size = map.keySet().stream().reduce(Integer::max).orElse(0);
        int days = 5;
        for (int i = 1; i <= size; i++) {
            double count = 0;
            for (int j = 1; j <= days; j++) {
                if (map.containsKey(i) && map.get(i).containsKey(j)) {
                    List<Vehicle> v = map.get(i).get(j);
                    count += distance(v);
                }
            }
            double avg = count / days;
            getOut().println("period: " + i + "\t distance: " + avg);
        }
    }


    private double distance(List<Vehicle> vehicles) {
        double dist = 0;
        if (vehicles != null && vehicles.size() > 1) {
            for (int i = 0; i < vehicles.size(); i++) {
                dist += getDist(vehicles.get(0), vehicles.get(1));
            }
        } else {
            return 0;
        }
        return dist / vehicles.size();
    }

    private double getDist(Vehicle v1, Vehicle v2) {
        double speed = calculateSpeed(v2);
        double dist = speed * (v2.getFrontAxleTime() - v1.getFrontAxleTime()) / oneHourMili;
        return dist;
    }

    private double calculateSpeed(Vehicle v) {
        long x = v.getBackAxleTime() - v.getFrontAxleTime();
        double y = ((2.5 / x) * oneHourMili) / 1000;
        return y;
    }


    @Override
    public void present(List<Vehicle> vehicles, boolean average) {
        if (vehicles == null) {
            getOut().println(" No cars went to any direction ");
        } else {
            Direction[] directions = new Direction[]{
                    Direction.NORTH_BOUND, Direction.SOUTH_BOUND
            };
            for (Direction direction : directions) {
                getOut().println(direction);

                Map<Integer, List<Vehicle>> vehiclesPerDay = vehicles.stream()
                        .filter(vehicle -> direction.equals(vehicle.getDirection()))
                        .collect(Collectors.groupingBy(Vehicle::getDay));

                if (vehiclesPerDay.isEmpty()) {
                    getOut().println(" No cars went to this direction ");
                } else {
                    if (!average) {
                        showPerPeriod(vehiclesPerDay, v -> v.getTwelveHourGroup() != 0, Vehicle::getTwelveHourGroup,
                                "12 Hours");
                        showPerPeriod(vehiclesPerDay, v -> v.getOneHourGroup() != 0, Vehicle::getOneHourGroup,
                                "1 Hour");
                        showPerPeriod(vehiclesPerDay, v -> v.getThirtyMinsGroup() != 0, Vehicle::getThirtyMinsGroup,
                                "30 Mins");
                        showPerPeriod(vehiclesPerDay, v -> v.getTwentyMinsGroup() != 0, Vehicle::getTwentyMinsGroup,
                                "20 Mins");
                        showPerPeriod(vehiclesPerDay, v -> v.getFifteenMinsGroup() != 0, Vehicle::getFifteenMinsGroup,
                                "15 Mins");
                    } else {
                        List<Vehicle> vehiclesPerDirection = vehicles.stream()
                                .filter(vehicle -> direction.equals(vehicle.getDirection()))
                                .collect(Collectors.toList());

                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getTwelveHourGroup() != 0,
                                Vehicle::getTwelveHourGroup, "12 Hours");
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getOneHourGroup() != 0,
                                Vehicle::getOneHourGroup, "1 Hour");
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getThirtyMinsGroup() != 0,
                                Vehicle::getThirtyMinsGroup, "30 Mins");
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getTwentyMinsGroup() != 0,
                                Vehicle::getTwentyMinsGroup, "20 Mins");
                        showAveragePerPeriod(vehiclesPerDirection, v -> v.getFifteenMinsGroup() != 0,
                                Vehicle::getFifteenMinsGroup, "15 Mins");
                    }
                }
            }
        }
    }
}
