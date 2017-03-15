package track.lessons.lesson4;

import org.junit.Assert;
import org.junit.Test;
import track.container.Container;
import track.container.JsonConfigReader;
import track.container.beans.Car;
import track.container.beans.Engine;
import track.container.beans.Gear;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;

import java.io.File;

public class ContainerTest {

    private static final String DEFAULT_PATH = "src/main/resources/config.json";
    private static final String DEFAULT_FAIL_PATH = "src/main/resources/config_fail.json";
    private static final String ENGINE_BEAN = "engineBean";
    private static final String GEAR_BEAN = "gearBean";
    private static final String CAR_BEAN = "carBean";

    @FunctionalInterface
    private interface Callback {
        void callback() throws Exception;
    }

    private void assertNotException(Callback callback) {
        try {
            callback.callback();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertFalse(true);
        }
    }

    @Test
    public void readJson() {
        ConfigReader reader = new JsonConfigReader();
        assertNotException(() -> reader.parseBeans(new File(DEFAULT_PATH)));
    }

    @Test
    public void readJsonFail() throws InvalidConfigurationException{
        ConfigReader reader = new JsonConfigReader();
        reader.parseBeans(new File(DEFAULT_FAIL_PATH));
    }


    @Test
    public void getEngine() {
        final int ENGINE_POWER = 200;

        ConfigReader reader = new JsonConfigReader();
        assertNotException(() -> {
            Container container = new Container(reader.parseBeans(new File(DEFAULT_PATH)));
            Engine engine = (Engine)container.getById(ENGINE_BEAN);
            Assert.assertEquals(engine.getPower(), ENGINE_POWER);
        });
    }

    @Test
    public void getGear() {
        final int GEAR_COUNT = 6;

        ConfigReader reader = new JsonConfigReader();
        assertNotException(() -> {
            Container container = new Container(reader.parseBeans(new File(DEFAULT_PATH)));
            Gear gear = (Gear)container.getById(GEAR_BEAN);
            Assert.assertEquals(gear.getCount(), GEAR_COUNT);
        });
    }



    @Test
    public void GetCar() {
        ConfigReader reader = new JsonConfigReader();

        assertNotException(() -> {
            Container container = new Container(reader.parseBeans(new File(DEFAULT_PATH)));
            Car car = (Car)container.getById(CAR_BEAN);
            Engine engine = (Engine) container.getById(ENGINE_BEAN);
            Gear gear = (Gear) container.getById(GEAR_BEAN);
            Assert.assertEquals(car.getEngine(), engine);
            Assert.assertEquals(car.getGear(), gear);
        });
    }


}
