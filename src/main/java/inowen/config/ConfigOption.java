package inowen.config;


/**
 * With T as Integer, Boolean, String or Double.
 * (other types won't work! The compiler will accept them, but internally they'll be handled as Strings when
 * reading from a config file, and throw casting exceptions at runtime, and that would be messy).
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
        if ((value instanceof Boolean)) {
            value = (T) Boolean.valueOf(asString);
        }
        else if (value instanceof Integer) {
            value = (T) Integer.valueOf(asString);
        }
        else if (value instanceof Double) {
            value = (T) Double.valueOf(asString);
        }
        else {
            value = (T)asString;
        }
    }

    public T getValue() { return value; }


}
