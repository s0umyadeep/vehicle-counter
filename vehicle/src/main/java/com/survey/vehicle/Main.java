package com.survey.vehicle;

import java.util.Collection;

import com.survey.vehicle.parser.DataParser;
import com.survey.vehicle.presenter.Presenter;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			System.out.println("Usage: java -jar vehicle-survey-0.0.1-SNAPSHOT.jar <datafile> <average>");
			return;
		}

		VehicleSurvey vehicleSurvey = new VehicleSurvey();
		Collection<Presenter> presenters = vehicleSurvey.configurePresenters();
		vehicleSurvey.setPresenters(presenters);
		vehicleSurvey.setAverage(false);
		vehicleSurvey.setParser(new DataParser());
		if (args.length >= 2) {
			String average = args[1];
			if ("true".equalsIgnoreCase(average)) {
				vehicleSurvey.setAverage(true);
			}
		}
		if (args.length >= 1) {
			String datafile = args[0];
			if (datafile != null && datafile.trim().length() > 0) {
				vehicleSurvey.process(datafile);
			}
		}
//
//		File file = new File(Main.class.getClassLoader().getResource("abc.txt").getFile());
//		vehicleSurvey.process(file.getAbsolutePath());
	}

}
