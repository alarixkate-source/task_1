package ru.yandex.praktikum9;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Юнит-тесты для класса Burger.
 * Проверяем основное: установку булочки, добавление/удаление/перемещение ингредиентов,
 * расчёт цены и формирование чека.
 */
public class BurgerTest {

    private Burger burger;

    @Mock
    private Bun mockBun;

    @Before
    public void setUp() {
        // Инициализируем моки
        MockitoAnnotations.openMocks(this);

        burger = new Burger();

        // Настраиваем мок булочки: фиксируем имя и цену
        when(mockBun.getName()).thenReturn("white bun");
        when(mockBun.getPrice()).thenReturn(200.0f);
    }

    /**
     * Проверяем, что setBuns() корректно сохраняет переданную булочку в поле burger.bun.
     */
    @Test
    public void testSetBuns() {
        burger.setBuns(mockBun);
        assertSame(mockBun, burger.bun);
    }

    /**
     * Проверяем добавление ингредиента
     */
    @Test
    public void testAddIngredient() {
        Ingredient realIngredient = new Ingredient(IngredientType.SAUCE, "sour cream", 200.0f);
        burger.addIngredient(realIngredient);

        assertEquals(1, burger.ingredients.size());
        assertTrue(burger.ingredients.contains(realIngredient));
    }

    /**
     * Проверяем удаление ингредиента
     */
    @Test
    public void testRemoveIngredient() {
        Ingredient real1 = new Ingredient(IngredientType.SAUCE, "sour cream", 200.0f);
        Ingredient real2 = new Ingredient(IngredientType.FILLING, "cutlet", 100.0f);

        burger.addIngredient(real1);
        burger.addIngredient(real2);
        assertEquals(2, burger.ingredients.size());

        burger.removeIngredient(0); // Удаляем первый элемент

        assertEquals(1, burger.ingredients.size());
        assertFalse(burger.ingredients.contains(real1));
        assertTrue(burger.ingredients.contains(real2));
    }

    /**
     * Проверяем, что при попытке удалить ингредиент по несуществующему индексу
     * выбрасывается IndexOutOfBoundsException (пустой список).
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientOutOfBounds() {
        burger.removeIngredient(0); // Пустой список — индекс 0 недопустим
    }

    /**
     * Проверяем перемещение ингредиента из одной позиции в другую.
     */
    @Test
    public void testMoveIngredient() {
        Ingredient real1 = new Ingredient(IngredientType.SAUCE, "sour cream", 200.0f);
        Ingredient real2 = new Ingredient(IngredientType.FILLING, "cutlet", 100.0f);
        Ingredient real3 = new Ingredient(IngredientType.FILLING, "dinosaur", 200.0f);

        burger.addIngredient(real1);
        burger.addIngredient(real2);
        burger.addIngredient(real3);

        // Перемещаем элемент с индекса 0 на индекс 2
        burger.moveIngredient(0, 2);

        // После перемещения порядок должен быть: real2, real3, real1
        assertEquals(real2, burger.ingredients.get(0));
        assertEquals(real3, burger.ingredients.get(1));
        assertEquals(real1, burger.ingredients.get(2));
    }

    /**
     * Проверяем, что нельзя переместить ингридиент на недопустимый инжекс
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientOutOfBounds() {
        Ingredient real = new Ingredient(IngredientType.SAUCE, "test", 10.0f);
        burger.addIngredient(real);
        burger.moveIngredient(0, 5); // Индекс 5 недопустим для списка из 1 элемента
    }

    /**
     * Проверяем сценарий, когда пытаемся переместить ингредиент на ту же позицию.
     * Список остается без изменений.
     */
    @Test
    public void testMoveIngredientSameIndex() {
        Ingredient real = new Ingredient(IngredientType.SAUCE, "s1", 10.0f);
        burger.addIngredient(real);

        burger.moveIngredient(0, 0); // Перемещение на ту же позицию

        assertEquals(1, burger.ingredients.size());
        assertSame(real, burger.ingredients.get(0));
    }

    /**
     * Тест расчёта цены с использованием моков.
     * Проверяем, что Burger корректно использует getPrice() у булочки и ингредиентов
     */
    @Test
    public void testGetPriceWithMocks() {
        // Создаём моки ингредиентов
        Ingredient mockIng1 = mock(Ingredient.class);
        Ingredient mockIng2 = mock(Ingredient.class);

        when(mockBun.getPrice()).thenReturn(200.0f);
        when(mockIng1.getPrice()).thenReturn(200.0f);
        when(mockIng2.getPrice()).thenReturn(100.0f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockIng1);
        burger.addIngredient(mockIng2);

        float expected = 2 * 200.0f + 200.0f + 100.0f; // 700.0
        assertEquals(expected, burger.getPrice(), 0.001f);

        // Убеждаемся, что методы getPrice() действительно были вызваны
        verify(mockBun).getPrice();
        verify(mockIng1).getPrice();
        verify(mockIng2).getPrice();
    }

    /**
     * Проверяем формирование чека (getReceipt()) с моками.
     * Проверяем наличие ключевых частей:
     * название булочки, упоминания ингредиентов, наличие цены, что бы не падал из-за мелких изменений чека
     */
    @Test
    public void testGetReceiptWithMocks() {
        Ingredient mockIng1 = mock(Ingredient.class);
        Ingredient mockIng2 = mock(Ingredient.class);

        when(mockBun.getName()).thenReturn("white bun");

        when(mockIng1.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIng1.getName()).thenReturn("sour cream");
        when(mockIng1.getPrice()).thenReturn(200.0f);

        when(mockIng2.getType()).thenReturn(IngredientType.FILLING);
        when(mockIng2.getName()).thenReturn("cutlet");
        when(mockIng2.getPrice()).thenReturn(100.0f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockIng1);
        burger.addIngredient(mockIng2);

        String receipt = burger.getReceipt();

        assertTrue("Чек должен содержать название булочки", receipt.contains("(==== white bun ====)"));
        assertTrue("Чек должен содержать упоминание соуса", receipt.contains("sauce"));
        assertTrue("Чек должен содержать упоминание начинки", receipt.contains("filling"));
        assertTrue("Чек должен содержать строку с ценой", receipt.contains("Price:"));
    }

    /**
     * Проверяем чек для бургера без ингредиентов.
     * В чеке есть название булочки и цена, но нет строк с ингредиентами.
     */
    @Test
    public void testGetReceiptWithNoIngredients() {
        burger.setBuns(mockBun);
        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("(==== white bun ====)"));

        // В чеке не должно быть упоминаний ингредиентов (sauce/filling)
        assertFalse(receipt.contains("sauce") || receipt.contains("filling"));

        // Цена должна быть равна цене булочки * 2 (400.0)
        assertTrue(receipt.contains("Price:"));
    }

    /**
     * Проверяем чек для бургера с одним ингредиентом.
     * В чеке должны быть: название булочки, строка с ингредиентом, цена.
     */
    @Test
    public void testGetReceiptWithOneIngredient() {
        Ingredient real = new Ingredient(IngredientType.SAUCE, "hot sauce", 100.0f);
        burger.setBuns(mockBun);
        burger.addIngredient(real);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("(==== white bun ====)"));
        assertTrue(receipt.contains("hot sauce"));
        assertTrue(receipt.contains("Price:"));
    }

    /**
     * Дополнительный тест: удаление первого ингредиента из списка.
     * Проверяем, что после удаления остаётся ожидаемый элемент.
     */
    @Test
    public void testRemoveIngredientFirst() {
        Ingredient real1 = new Ingredient(IngredientType.SAUCE, "s1", 10.0f);
        Ingredient real2 = new Ingredient(IngredientType.SAUCE, "s2", 20.0f);

        burger.addIngredient(real1);
        burger.addIngredient(real2);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertEquals(real2, burger.ingredients.get(0)); // Остался второй элемент
    }

    /**
     * Удаление последнего ингредиента из списка.
     * Проверяем, что остался первый элемент.
     */
    @Test
    public void testRemoveIngredientLast() {
        Ingredient real1 = new Ingredient(IngredientType.SAUCE, "s1", 10.0f);
        Ingredient real2 = new Ingredient(IngredientType.SAUCE, "s2", 20.0f);

        burger.addIngredient(real1);
        burger.addIngredient(real2);

        // Удаляем последний элемент (индекс 1)
        burger.removeIngredient(1);

        assertEquals(1, burger.ingredients.size());
        assertEquals(real1, burger.ingredients.get(0)); // Остался первый элемент
    }
}