package inowen.config;


/**
 * With T as Integer, Boolean, String or Double.
 * @param <T>
 */
public class ConfigOption<T> {

    public String name = "";
    public T value = null;
    public T defaultValue = null;

    public ConfigOption(String name, T value, T defaultValue) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    /**
     * Sets Integer, Boolean, String or Double.
     * @param asString
     */
    public void setValue(String asString) {
        if ((value instanceof Boolean) || (defaultValue instanceof Boolean)) {
            value = (T)Boolean.valueOf(asString);
        }
        else {
            value = (T)asString;
        }
    }

    public T getValue() { return value; }


}
