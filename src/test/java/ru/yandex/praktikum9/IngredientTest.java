package ru.yandex.praktikum9;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Юнит-тесты для класса Ingredient (ингредиент).
 */
public class IngredientTest {

    /**
     * Проверяем, что конструктор сохраняет тип, имя и цену,
     * а геттеры возвращают эти значения.
     */
    @Test
    public void testIngredientConstructorAndGetters() {
        // Создаём тестовый ингредиент
        Ingredient ingredient = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);

        // Проверяем тип ингредиента
        assertEquals(IngredientType.SAUCE, ingredient.getType());

        // Проверяем имя ингредиента
        assertEquals("hot sauce", ingredient.getName());

        // Проверяем цену
        assertEquals(100.0f, ingredient.getPrice(), 0.001f);
    }
}
