package com.survey.vehicle.parser;

import com.survey.vehicle.Main;
import com.survey.vehicle.model.Vehicle;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class DataParserTest {

    @Test(expected = DataParsingException.class)
    public void testParseNull() throws Exception {
        DataParser parser = new DataParser();
        parser.parse(null);
    }
    @Test(expected = DataParsingException.class)
    public void testParseEmpty() throws Exception {
        DataParser parser = new DataParser();
        parser.parse("");
    }
    @Test
    public void testParse() throws Exception {
        DataParser parser = new DataParser();
        File file = new File(Main.class.getClassLoader().getResource("abc.txt").getFile());
        List<Vehicle> vehicles = parser.parse(file.getAbsolutePath());
        Assert.assertEquals(vehicles.size(), 3);
        Assert.assertEquals(vehicles.stream().findFirst().get().getDay(), 1);

    }


}
