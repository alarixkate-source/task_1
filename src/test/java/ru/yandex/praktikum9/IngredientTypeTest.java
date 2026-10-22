package ru.yandex.praktikum9;

import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientTypeTest {

    @Test
    public void testEnumValuesCount() {
        IngredientType[] values = IngredientType.values();
        assertEquals(2, values.length);
    }

    @Test
    public void testEnumValueOf() {
        assertEquals(IngredientType.SAUCE, IngredientType.valueOf("SAUCE"));
        assertEquals(IngredientType.FILLING, IngredientType.valueOf("FILLING"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnumValueOfInvalid() {
        IngredientType.valueOf("INVALID");
    }
}