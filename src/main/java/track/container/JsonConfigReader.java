package track.container;

import org.json.JSONArray;
import org.json.JSONObject;
import track.container.config.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Реализовать
 */
public class JsonConfigReader implements ConfigReader {


    private Property readPropertyFromJson(JSONObject jsonProperty) {
        return new Property(jsonProperty.getString("name"),
                jsonProperty.getString("value"),
                jsonProperty.getEnum(ValueType.class, "type"));
    }

    private Map<String, Property> readCarProperties(JSONObject carObject) {
        Map<String, Property> resultProperty = new HashMap<>();
        resultProperty.put("gear", readPropertyFromJson(carObject.getJSONObject("gear")));
        resultProperty.put("engine", readPropertyFromJson(carObject.getJSONObject("engine")));
        return resultProperty;
    }

    private Map<String,Property> readGearProperties(JSONObject gearObject) {
        Map<String, Property> resultProperty = new HashMap<>();
        resultProperty.put("count", readPropertyFromJson(gearObject.getJSONObject("count")));
        return resultProperty;
    }

    private Map<String,Property> readEngineProperties(JSONObject engineObject) {
        Map<String, Property> resultProperty = new HashMap<>();
        resultProperty.put("power", readPropertyFromJson(engineObject.getJSONObject("power")));
        return resultProperty;
    }

    @Override
    public List<Bean> parseBeans(File configFile) throws InvalidConfigurationException {

        String jsonString = "";
        try {
            StringBuilder builder = new StringBuilder();
            Files.readAllLines(configFile.toPath()).forEach(builder::append);
            jsonString = builder.toString();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray beans = jsonObject.getJSONArray("beans");

        List<Bean> result = new ArrayList<>();

        for (int i = 0; i < beans.length(); i++) {
            JSONObject jsonBean = beans.getJSONObject(i);
            JSONObject jsonProperties = jsonBean.getJSONObject("properties");

            String beanId = jsonBean.getString("id");
            Map<String, Property> properties;

            switch (beanId) {
                case "carBean":
                    properties = readCarProperties(jsonProperties);
                    break;
                case "engineBean":
                    properties = readEngineProperties(jsonProperties);
                    break;
                case "gearBean":
                    properties = readGearProperties(jsonProperties);
                    break;
                default:
                    throw new InvalidConfigurationException("");
            }

            Bean bean = new Bean(jsonBean.getString("id"),
                    jsonBean.getString("className"), properties);
            result.add(bean);
        }

        return result;
    }
}
