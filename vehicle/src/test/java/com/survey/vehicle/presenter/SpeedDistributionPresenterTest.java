package com.survey.vehicle.presenter;

import com.survey.vehicle.model.Direction;
import com.survey.vehicle.model.Vehicle;
import com.survey.vehicle.presenter.SpeedDistributionPresenter;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class SpeedDistributionPresenterTest {
    
    @Test
    public void testSpeedDistributionIsCalculatedCorrectly() throws Exception {
        SpeedDistributionPresenter speedDistributionPresenter = new SpeedDistributionPresenter();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(stream);) {
            speedDistributionPresenter.setOut(out);

            List<Vehicle> vehicles = new LinkedList<>();
            Vehicle v1 = new Vehicle();
            v1.setDay(1);
            v1.setDirection(Direction.NORTH_BOUND);
            v1.setFifteenMinsGroup(1);
            v1.setFrontAxleTime(7200000);
            v1.setBackAxleTime(7200100);
            vehicles.add(v1);

            Vehicle v2 = new Vehicle();
            v2.setDay(1);
            v2.setDirection(Direction.NORTH_BOUND);
            v2.setFifteenMinsGroup(12);
            v2.setFrontAxleTime(7200200);
            v2.setBackAxleTime(7200300);
            vehicles.add(v2);

            Vehicle v3 = new Vehicle();
            v3.setDay(1);
            v3.setDirection(Direction.NORTH_BOUND);
            v3.setFifteenMinsGroup(12);
            v3.setFrontAxleTime(7200400);
            v3.setBackAxleTime(7200500);
            vehicles.add(v3);

            Vehicle v4 = new Vehicle();
            v4.setDay(1);
            v4.setDirection(Direction.NORTH_BOUND);
            v4.setFifteenMinsGroup(12);
            v4.setFrontAxleTime(7200400);
            v4.setBackAxleTime(7200500);
            vehicles.add(v4);

            speedDistributionPresenter.present(vehicles, false);
            String message = new String(stream.toByteArray());
            Assert.assertTrue(message.contains("period: 12\t speed: 90.0"));


        }
    }

    @Test
    public void testAverageSpeedDistributionIsCalculatedCorrectly() throws Exception {
        SpeedDistributionPresenter speedDistributionPresenter = new SpeedDistributionPresenter();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(stream);) {
            speedDistributionPresenter.setOut(out);

            List<Vehicle> vehicles = new LinkedList<>();
            Vehicle v1 = new Vehicle();
            v1.setDay(1);
            v1.setDirection(Direction.NORTH_BOUND);
            v1.setFifteenMinsGroup(1);
            v1.setFrontAxleTime(7200000);
            v1.setBackAxleTime(7200100);
            vehicles.add(v1);

            Vehicle v2 = new Vehicle();
            v2.setDay(1);
            v2.setDirection(Direction.NORTH_BOUND);
            v2.setFifteenMinsGroup(12);
            v2.setFrontAxleTime(7200200);
            v2.setBackAxleTime(7200300);
            vehicles.add(v2);

            Vehicle v3 = new Vehicle();
            v3.setDay(2);
            v3.setDirection(Direction.NORTH_BOUND);
            v3.setFifteenMinsGroup(12);
            v3.setFrontAxleTime(7200800);
            v3.setBackAxleTime(7201000);
            vehicles.add(v3);

            Vehicle v4 = new Vehicle();
            v4.setDay(2);
            v4.setDirection(Direction.NORTH_BOUND);
            v4.setFifteenMinsGroup(12);
            v4.setFrontAxleTime(8201100);
            v4.setBackAxleTime(8201600);
            vehicles.add(v4);

            speedDistributionPresenter.present(vehicles, true);
            String message = new String(stream.toByteArray());
            Assert.assertTrue(message.contains("period: 12\t speed: 24.3"));
        }
    }

    @Test
    public void testEmptyVehicleListIsHandled() throws Exception {
        SpeedDistributionPresenter speedDistributionPresenter = new SpeedDistributionPresenter();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(stream);) {
            speedDistributionPresenter.setOut(out);
            speedDistributionPresenter.present(new LinkedList<>(), true);
            String message = new String(stream.toByteArray());

            Assert.assertTrue(message.contains("NORTH_BOUND"));
            Assert.assertTrue(message.contains("SOUTH_BOUND"));
            Assert.assertTrue(message.contains("No cars went to this direction"));
        }
    }

    @Test
    public void testNullVehicleListIsHandled() throws Exception {
        SpeedDistributionPresenter speedDistributionPresenter = new SpeedDistributionPresenter();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(stream);) {
            speedDistributionPresenter.setOut(out);
            speedDistributionPresenter.present(null, true);
            Assert.assertTrue(new String(stream.toByteArray()).contains("No cars went to this direction"));

        }

    }
}
