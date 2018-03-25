package com.survey.vehicle.parser;

import com.survey.vehicle.model.Direction;
import com.survey.vehicle.model.Vehicle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataParser implements Parser {

    @Override
    public List<Vehicle> parse(String path) throws DataParsingException {
        if (path == null || path.trim().length() <= 0) {
            throw new DataParsingException("Path is null");
        }
        List<Vehicle> vehicles = new LinkedList<>();
        try (BufferedReader buffered = new BufferedReader(new FileReader(new File(path)))) {

            Stream<String> data = buffered.lines();
            if (data != null) {
                int count = 0;
                long[] temp = new long[4];
                Param param = new Param(temp, count);
                data.forEach(line -> {
                    try {
                        process(vehicles, param, line);
                    } catch (DataParsingException e) {
                        throw new RuntimeException(e);
                    }
                });


            }
        } catch (Exception e) {
            throw new DataParsingException(e);
        }

        updateWithDays(vehicles);

        updateWithTwelveHourPeriod(vehicles);
        updateWithOneHourPeriod(vehicles);
        updateWithThirtyMinPeriod(vehicles);
        updateWithTwentyMinPeriod(vehicles);
        updateWithFifteenMinPeriod(vehicles);

        return vehicles;
    }

    public void updateWithDays(List<Vehicle> vehicles) {
        int day = 1;
        Vehicle prev = null;
        for (Vehicle vehicle : vehicles) {
            if (prev == null) {
                prev = vehicle;
                vehicle.setDay(day);
                continue;
            }
            if (prev.getFrontAxleTime() > vehicle.getFrontAxleTime()) {
                vehicle.setDay(++day);
            } else {
                vehicle.setDay(day);
            }
            prev = vehicle;
        }
    }


    private void updateWithPeriods(List<Vehicle> vehicles, long totalPeriod, Consumer<PeriodParam> updater) {
        Map<Integer, List<Vehicle>> vehiclesPerDayMap =
                vehicles.stream().collect(Collectors.groupingBy(Vehicle::getDay));
        for (Integer day : vehiclesPerDayMap.keySet()) {
            List<Vehicle> vehiclesPerDay = vehiclesPerDayMap.get(day);
            for (int i = 0; i < totalPeriod; i++) {
                for (Vehicle vehicle : vehiclesPerDay) {
                    PeriodParam periodParam = new PeriodParam();
                    periodParam.setCurrentPeriod(i);
                    periodParam.setVehicle(vehicle);
                    updater.accept(periodParam);
                }
            }
        }
    }

    public void updateWithTwelveHourPeriod(List<Vehicle> vehiclesPerDay) {
        long oneHourInMili = 1000 * 60 * 60;
        long twelveHourPeriod = oneHourInMili * 12;
        long periods = oneHourInMili * 24 / twelveHourPeriod;

        updateWithPeriods(vehiclesPerDay, periods, periodParam -> {
            long time = periodParam.getVehicle().getFrontAxleTime();
            if (time <= twelveHourPeriod * (periodParam.getCurrentPeriod() + 1)
                    && periodParam.getVehicle().getTwelveHourGroup() == 0) {
                periodParam.getVehicle().setTwelveHourGroup(periodParam.getCurrentPeriod() + 1);
            }
        });
    }

    public void updateWithOneHourPeriod(List<Vehicle> vehiclesPerDay) {
        long oneHourInMili = 1000 * 60 * 60;
        long oneHourPeriod = oneHourInMili;
        long periods = oneHourInMili * 24 / oneHourPeriod;
        updateWithPeriods(vehiclesPerDay, periods, periodParam -> {
            long time = periodParam.getVehicle().getFrontAxleTime();
            if (time <= oneHourPeriod * (periodParam.getCurrentPeriod() + 1)
                    && periodParam.getVehicle().getOneHourGroup() == 0) {
                periodParam.getVehicle().setOneHourGroup(periodParam.getCurrentPeriod() + 1);
            }
        });
    }

    public void updateWithThirtyMinPeriod(List<Vehicle> vehiclesPerDay) {
        long oneHourInMili = 1000 * 60 * 60;
        long thirtyMinPeriod = oneHourInMili / 2;
        long periods = oneHourInMili * 24 / thirtyMinPeriod;
        updateWithPeriods(vehiclesPerDay, periods, periodParam -> {
            long time = periodParam.getVehicle().getFrontAxleTime();
            if (time <= thirtyMinPeriod * (periodParam.getCurrentPeriod() + 1)
                    && periodParam.getVehicle().getThirtyMinsGroup() == 0) {
                periodParam.getVehicle().setThirtyMinsGroup(periodParam.getCurrentPeriod() + 1);
            }
        });
    }

    public void updateWithTwentyMinPeriod(List<Vehicle> vehiclesPerDay) {
        long oneHourInMili = 1000 * 60 * 60;
        long twentyMinPeriod = 1000 * 60 * 20;
        long periods = oneHourInMili * 24 / twentyMinPeriod;

        updateWithPeriods(vehiclesPerDay, periods, periodParam -> {
            long time = periodParam.getVehicle().getFrontAxleTime();
            if (time <= twentyMinPeriod * (periodParam.getCurrentPeriod() + 1)
                    && periodParam.getVehicle().getTwentyMinsGroup() == 0) {
                periodParam.getVehicle().setTwentyMinsGroup(periodParam.getCurrentPeriod() + 1);
            }
        });
    }

    public void updateWithFifteenMinPeriod(List<Vehicle> vehiclesPerDay) {
        long oneHourInMili = 1000 * 60 * 60;
        long fifteenMinPeriod = 1000 * 60 * 15;
        long periods = oneHourInMili * 24 / fifteenMinPeriod;

        updateWithPeriods(vehiclesPerDay, periods, periodParam -> {
            long time = periodParam.getVehicle().getFrontAxleTime();
            if (time <= fifteenMinPeriod * (periodParam.getCurrentPeriod() + 1)
                    && periodParam.getVehicle().getFifteenMinsGroup() == 0) {
                periodParam.getVehicle().setFifteenMinsGroup(periodParam.getCurrentPeriod() + 1);
            }
        });
    }


    private void process(List<Vehicle> vehicles, Param param, String line) throws DataParsingException {

        param.setCount(param.getCount() + 1);
        if (line != null && line.trim().length() > 0) {
            String dir = String.valueOf(line.charAt(0));
            String time = line.substring(1);
            param.getTemp()[(int) param.getCount() - 1] = Long.parseLong(time);

            if (dir.equalsIgnoreCase("A") && param.getCount() == 2) {

                Vehicle vehicle = new Vehicle();
                vehicle.setFrontAxleTime(param.getTemp()[0]);
                vehicle.setBackAxleTime(param.getTemp()[1]);
                vehicle.setDirection(Direction.NORTH_BOUND);
                vehicles.add(vehicle);

                param.setTemp(new long[4]);
                param.setCount(0);


            } else if (dir.equalsIgnoreCase("B") && param.getCount() == 4) {

                Vehicle vehicle = new Vehicle();
                vehicle.setFrontAxleTime(param.getTemp()[0]);
                vehicle.setBackAxleTime(param.getTemp()[2]);
                vehicle.setDirection(Direction.SOUTH_BOUND);
                vehicles.add(vehicle);

                param.setTemp(new long[4]);
                param.setCount(0);

            } else if (dir.equalsIgnoreCase("A") && param.getCount() == 4) {
                throw new DataParsingException("Data not valid");
            } else if (dir.equalsIgnoreCase("B") && param.getCount() % 2 == 1) {
                throw new DataParsingException("Data not valid");
            }
        }

    }


    private class Param {
        private long[] temp = new long[4];
        private long count = 0;

        public Param(long[] temp, long count) {
            this.temp = temp;
            this.count = count;
        }

        public long[] getTemp() {
            return temp;
        }

        public void setTemp(long[] temp) {
            this.temp = temp;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    private class PeriodParam {
        private Vehicle vehicle;
        private int currentPeriod;

        public Vehicle getVehicle() {
            return vehicle;
        }

        public void setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        public int getCurrentPeriod() {
            return currentPeriod;
        }

        public void setCurrentPeriod(int currentPeriod) {
            this.currentPeriod = currentPeriod;
        }
    }
}
