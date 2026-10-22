package ru.yandex.praktikum9;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Параметризованные тесты для проверки расчёта цены бургера (getPrice()).
 */
@RunWith(Parameterized.class)
public class BurgerPriceParameterizedTest {

    private final Bun bun;
    private final List<Ingredient> ingredients;
    private final float expectedPrice;

    /**
     * Конструктор для передачи параметров в тест.
     */
    public BurgerPriceParameterizedTest(Bun bun, List<Ingredient> ingredients, float expectedPrice) {
        this.bun = bun;
        this.ingredients = ingredients;
        this.expectedPrice = expectedPrice;
    }

    /**
     * Возвращает коллекцию тестовых данных: булочка, список ингредиентов, ожидаемая цена.
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Сценарий 1: булочка без ингредиентов
                // Цена = цена булочки * 2
                {new Bun("white bun", 100.0f), List.of(), 200.0f},

                // Сценарий 2: одна булочка и один соус
                // Цена = 100*2 + 50 = 250
                {new Bun("white bun", 100.0f),
                        List.of(new Ingredient(IngredientType.SAUCE, "hot sauce", 50.0f)),
                        250.0f},

                // Сценарий 3: булочка и два ингредиента (соус + начинка)
                // Цена = 150*2 + 80 + 120 = 500
                {new Bun("black bun", 150.0f),
                        List.of(
                                new Ingredient(IngredientType.SAUCE, "chili", 80.0f),
                                new Ingredient(IngredientType.FILLING, "cutlet", 120.0f)
                        ),
                        500.0f},

                // Сценарий 4: булочка и три ингредиента
                // Цена = 200*2 + 60 + 150 + 70 = 680
                {new Bun("red bun", 200.0f),
                        List.of(
                                new Ingredient(IngredientType.SAUCE, "sour cream", 60.0f),
                                new Ingredient(IngredientType.FILLING, "dinosaur", 150.0f),
                                new Ingredient(IngredientType.SAUCE, "hot sauce", 70.0f)
                        ),
                        680.0f}
        });
    }

    /**
     * Основной тест: собираем бургер из переданных данных
     */
    @Test
    public void testGetPrice() {
        Burger burger = new Burger();
        burger.setBuns(bun);
        for (Ingredient ing : ingredients) {
            burger.addIngredient(ing);
        }
        assertEquals(expectedPrice, burger.getPrice(), 0.001f);
    }
}
