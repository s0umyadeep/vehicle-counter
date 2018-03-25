package com.survey.vehicle;

import com.survey.vehicle.handler.Presenter;
import com.survey.vehicle.model.Vehicle;
import com.survey.vehicle.parser.DataParsingException;
import com.survey.vehicle.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class VehicleSurveyTest {

    @Test
    public void testParserAndPresentersAreCalled() throws Exception {
        VehicleSurvey vehicleSurvey = new VehicleSurvey();
        TestClass t = new TestClass();
        Collection<Presenter> presenters = Arrays.asList(new Presenter() {
            @Override
            public void present(List<Vehicle> vehicles, boolean average) {
                Assert.assertNotNull(vehicles);
                Assert.assertEquals(2, vehicles.size());
                t.setCalled(true);
            }
        });
        vehicleSurvey.setPresenters(presenters);
        vehicleSurvey.setAverage(true);
        vehicleSurvey.setParser(new Parser() {
            @Override
            public List<Vehicle> parse(String path) throws DataParsingException {
                return Arrays.asList(new Vehicle(), new Vehicle());
            }
        });

        vehicleSurvey.process("");
        Assert.assertTrue(t.isCalled());
    }

    @Test
    public void testPresentersNotCalledWhenParseExceptionOccurs() {
        VehicleSurvey vehicleSurvey = new VehicleSurvey();
        TestClass t = new TestClass();
        Collection<Presenter> presenters = Arrays.asList(new Presenter() {
            @Override
            public void present(List<Vehicle> vehicles, boolean average) {
                t.setCalled(true);
            }
        });
        vehicleSurvey.setPresenters(presenters);
        vehicleSurvey.setAverage(true);
        vehicleSurvey.setParser(new Parser() {
            @Override
            public List<Vehicle> parse(String path) throws DataParsingException {
                throw new DataParsingException("Test");
            }
        });
        try {
            vehicleSurvey.process("");
        } catch (Exception e) {

        }

        Assert.assertFalse(t.isCalled());
    }

    class TestClass {
        private boolean called;

        public boolean isCalled() {
            return called;
        }

        public void setCalled(boolean called) {
            this.called = called;
        }
    }
}
