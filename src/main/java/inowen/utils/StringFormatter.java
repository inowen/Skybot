package inowen.utils;

public class StringFormatter {

    /**
     * Process strings like the following: "minecraft.effects.strength" to return
     * only the part after the last point. If there are no points, the same string is returned.
     * @param original
     * @return String
     */
    public static String removePointPath(String original) {
        String result = "";
        int readPos = original.length()-1;

        while(readPos>=0 && original.charAt(readPos)!='.') {
            result = result.concat(String.valueOf(original.charAt(readPos)));
            readPos--;
        }

        return new StringBuilder(result).reverse().toString();
    }
}
