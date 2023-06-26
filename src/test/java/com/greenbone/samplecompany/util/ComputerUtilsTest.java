package com.greenbone.samplecompany.util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class ComputerUtilsTest {

    @Test
    public void testGetValue_WithValidPatternName_ShouldReturnPatternValue() throws NoSuchFieldException {
        String expectedPattern = "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$";

        String actualPattern = ComputerUtils.getValue(ComputerUtils.class, PatternName.IPV4_PATTERN);

        Assertions.assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void testGetValue_WithInvalidPatternName_ShouldReturnNull() throws NoSuchFieldException {
        String actualPattern = ComputerUtils.getValue(ComputerUtils.class, PatternName.INVALID_PATTERN);

        Assertions.assertNull(actualPattern);
    }

    @Test
    public void testGetValue_WithNonAnnotatedField_ShouldReturnNull() throws NoSuchFieldException {
        String actualPattern = ComputerUtils.getValue(ComputerUtils.class, PatternName.NON_ANNOTATED_FIELD);

        Assertions.assertNull(actualPattern);
    }

    @Test
    public void testGetValue_WithPrivateFieldAccess_ShouldReturnPatternValue() throws NoSuchFieldException, IllegalAccessException {
        Field field = ComputerUtils.class.getDeclaredField("IPV4_PATTERN");
        field.setAccessible(true);
        String expectedPattern = (String) field.get(null);

        String actualPattern = ComputerUtils.getValue(ComputerUtils.class, PatternName.IPV4_PATTERN);

        Assertions.assertEquals(expectedPattern, actualPattern);
    }
}
