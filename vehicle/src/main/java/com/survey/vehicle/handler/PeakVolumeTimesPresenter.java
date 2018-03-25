package com.survey.vehicle.handler;

import com.survey.vehicle.model.Direction;
import com.survey.vehicle.model.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PeakVolumeTimesPresenter extends AbstractPresenter {

    private void showPeakPeriod(List<Vehicle> vehicles, Function<? super Vehicle, Boolean> filter,
                                Function<? super Vehicle, Integer> groupBy) {

        Map<Integer, Map<Integer, Long>> map = vehicles.stream().filter(v -> filter.apply(v))
                .collect(Collectors.groupingBy(Vehicle::getDay, Collectors.groupingBy(groupBy, Collectors.counting())));

        int days = 5;
        for (int j = 1; j <= days; j++) {
            long count = 0;
            Integer peakPeriod = 0;
            Map<Integer, Long> v = map.get(j);

            if (v != null) {
                for (int i = 1; i <= v.size(); i++) {
                    if (map.containsKey(j) && map.get(j).containsKey(i) && map.get(j).get(i) >= count) {
                        count = map.get(j).get(i);
                    }
                    peakPeriod = i;

                }
            }
            getOut().println("Day: " + getDayStr(j) + "\t Peak Period: " + peakPeriod);
        }
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

                List<Vehicle> vehiclesPerDirection =
                        vehicles.stream().filter(vehicle -> direction.equals(vehicle.getDirection()))
                                .collect(Collectors.toList());

                if (vehiclesPerDirection.isEmpty()) {
                    getOut().println(" No cars went to this direction ");
                } else {
                    showPeakPeriod(vehiclesPerDirection, v -> v.getTwelveHourGroup() != 0, Vehicle::getTwelveHourGroup);
                    showPeakPeriod(vehiclesPerDirection, v -> v.getOneHourGroup() != 0, Vehicle::getOneHourGroup);
                    showPeakPeriod(vehiclesPerDirection, v -> v.getThirtyMinsGroup() != 0, Vehicle::getThirtyMinsGroup);
                    showPeakPeriod(vehiclesPerDirection, v -> v.getTwentyMinsGroup() != 0, Vehicle::getTwentyMinsGroup);
                    showPeakPeriod(vehiclesPerDirection, v -> v.getFifteenMinsGroup() != 0,
                            Vehicle::getFifteenMinsGroup);
                }
            }

        }
    }
}
