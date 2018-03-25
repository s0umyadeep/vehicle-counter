package com.survey.vehicle.presenter;

import com.survey.vehicle.model.Direction;
import com.survey.vehicle.model.Vehicle;
import com.survey.vehicle.presenter.PeakVolumeTimesPresenter;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class PeakVolumeTimesPresenterTest {

    @Test
    public void testPeakVolumeTimeIsCalculatedCorrectly() throws Exception {
        PeakVolumeTimesPresenter PeakVolumeTimesPresenter = new PeakVolumeTimesPresenter();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(stream);) {
            PeakVolumeTimesPresenter.setOut(out);

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

            PeakVolumeTimesPresenter.present(vehicles, false);
            String message = new String(stream.toByteArray());
            System.out.println(message);
            Assert.assertTrue(message.contains("Day: Mon\t Peak Period: 2"));

        }
    }


    @Test
    public void testEmptyVehicleListIsHandled() throws Exception {
        PeakVolumeTimesPresenter PeakVolumeTimesPresenter = new PeakVolumeTimesPresenter();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(stream);) {
            PeakVolumeTimesPresenter.setOut(out);
            PeakVolumeTimesPresenter.present(new LinkedList<>(), true);
            String message = new String(stream.toByteArray());

            Assert.assertTrue(message.contains("NORTH_BOUND"));
            Assert.assertTrue(message.contains("SOUTH_BOUND"));
            Assert.assertTrue(message.contains("No cars went to this direction"));
        }
    }

    @Test
    public void testNullVehicleListIsHandled() throws Exception {
        PeakVolumeTimesPresenter PeakVolumeTimesPresenter = new PeakVolumeTimesPresenter();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(stream);) {
            PeakVolumeTimesPresenter.setOut(out);
            PeakVolumeTimesPresenter.present(null, true);
            Assert.assertTrue(new String(stream.toByteArray()).contains("No cars went to any direction"));
        }

    }
}
