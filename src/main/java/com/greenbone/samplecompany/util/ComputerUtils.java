package com.greenbone.samplecompany.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@interface CustomPatterns {
}
public class ComputerUtils {

    //https://mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
    @CustomPatterns
    public static final String IPV4_PATTERN =
            "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                    "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                    "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                    "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$";


    //https://uibakery.io/regex-library/mac-address-regex-java
    @CustomPatterns
    public static final String MAC_PATTERN = "^(?:[0-9A-Fa-f]{2}[:-]){5}(?:[0-9A-Fa-f]{2})$";

    public static String getValue(Class<?> clazz, PatternName patternName) {
        try {
            Field field = clazz.getDeclaredField(String.valueOf(patternName));
            if (field.isAnnotationPresent(CustomPatterns.class)) {
                field.setAccessible(true);
                return (String) field.get(null);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();}
        return null;
    }
}
