package upe.profile.rest.generator;

public class JavaHelper {
    public static String firstToLowerCase(String str) {
        if( str.length() == 1 ) {
            return str.toLowerCase();
        }
        return str.substring(0,1).toLowerCase()+str.substring(1);
    }

    public static String firstToUpperCase(String str) {
        if( str.length() == 1 ) {
            return str.toLowerCase();
        }
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

}
