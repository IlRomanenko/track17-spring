package track.container;

import track.container.config.Bean;
import track.container.config.InvalidConfigurationException;
import track.container.config.Property;
import track.container.config.ValueType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {


    private Map<String, Object> instanceObjects = new HashMap<>();
    private Map<String, String> classNameToId = new HashMap<>();

    private void createObjects(List<Bean> beans) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Bean bean : beans) {
            classNameToId.put(bean.getClassName(), bean.getId());
            instanceObjects.put(bean.getId(), Class.forName(bean.getClassName()).newInstance());
        }

    }

    private void parseBeans(List<Bean> beans) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchFieldException, InvalidConfigurationException,
            NoSuchMethodException, InvocationTargetException {

        createObjects(beans);

        for (Bean bean : beans) {
            Object currentObject = getById(bean.getId());
            Class<?> clazz = currentObject.getClass();

            for (Property property : bean.getProperties().values()) {
                StringBuilder propertyName = new StringBuilder(property.getName());
                Field curField = clazz.getDeclaredField(propertyName.toString());
                propertyName.setCharAt(0, Character.toUpperCase(propertyName.charAt(0)));


                Method setMethod = clazz.getDeclaredMethod("set" + propertyName, curField.getType());

                if (property.getType() == ValueType.VAL) {
                    switch (curField.getType().toString()) {
                        case "int":
                            setMethod.invoke(currentObject, Integer.parseInt(property.getValue()));
                            break;
                        default:
                            throw new InvalidConfigurationException("Can create only int fields");
                    }
                } else {
                    setMethod.invoke(currentObject, getByClass(curField.getType().getTypeName()));
                }
            }


        }
    }

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) throws InvalidConfigurationException {
        try {
            parseBeans(beans);
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException |
                NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            throw new InvalidConfigurationException(e.getMessage());
        }
    }

    /**
     * Вернуть объект по имени бина из конфига
     * Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) {
        return instanceObjects.get(id);
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) {
        return getById(classNameToId.get(className));
    }
}
