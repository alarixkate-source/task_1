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

    @Mock
    private Ingredient mockIng1;

    @Mock
    private Ingredient mockIng2;

    @Mock
    private Ingredient mockIng3;

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
        burger.addIngredient(mockIng1);
        assertEquals(1, burger.ingredients.size());
        assertTrue(burger.ingredients.contains(mockIng1));
    }

    /**
     * Проверяем удаление ингредиента
     */
    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(mockIng1);
        burger.addIngredient(mockIng2);
        assertEquals(2, burger.ingredients.size());
        burger.removeIngredient(0);
        assertEquals(1, burger.ingredients.size());
        assertFalse(burger.ingredients.contains(mockIng1));
        assertTrue(burger.ingredients.contains(mockIng2));
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
        burger.addIngredient(mockIng1);
        burger.addIngredient(mockIng2);
        burger.addIngredient(mockIng3);

        burger.moveIngredient(0, 2);

        assertEquals(mockIng2, burger.ingredients.get(0));
        assertEquals(mockIng3, burger.ingredients.get(1));
        assertEquals(mockIng1, burger.ingredients.get(2));
    }

    /**
     * Проверяем, что нельзя переместить ингридиент на недопустимый индекс
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientOutOfBounds() {
        burger.addIngredient(mockIng1);
        burger.moveIngredient(0, 5);
    }

    /**
     * Проверяем сценарий, когда пытаемся переместить ингредиент на ту же позицию.
     * Список остается без изменений.
     */
    @Test
    public void testMoveIngredientSameIndex() {
        burger.addIngredient(mockIng1);
        burger.moveIngredient(0, 0);
        assertEquals(1, burger.ingredients.size());
        assertSame(mockIng1, burger.ingredients.get(0));
    }



    /**
     * Тест расчёта цены с использованием моков.
     * Проверяем, что Burger корректно использует getPrice() у булочки и ингредиентов
     */
    @Test
    public void testGetPriceWithMocks() {
        when(mockIng1.getPrice()).thenReturn(200.0f);
        when(mockIng2.getPrice()).thenReturn(100.0f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockIng1);
        burger.addIngredient(mockIng2);

        float expected = 2 * 200.0f + 200.0f + 100.0f;
        assertEquals(expected, burger.getPrice(), 0.001f);

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

        assertTrue(receipt.contains("(==== white bun ====)"));
        assertTrue(receipt.contains("sauce"));
        assertTrue(receipt.contains("filling"));
        assertTrue(receipt.contains("Price:"));
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
        assertFalse(receipt.contains("sauce") || receipt.contains("filling"));
        assertTrue(receipt.contains("Price:"));
        // Проверим, что цена равна 400.0 (2 * 200)
        assertTrue(receipt.contains(String.format("%f", 400.0f)));
    }

    /**
     * Проверяем чек для бургера с одним ингредиентом.
     * В чеке должны быть: название булочки, строка с ингредиентом, цена.
     */
    @Test
    public void testGetReceiptWithOneIngredient() {
        when(mockIng1.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIng1.getName()).thenReturn("hot sauce");
        when(mockIng1.getPrice()).thenReturn(100.0f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockIng1);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("(==== white bun ====)"));
        assertTrue(receipt.contains("hot sauce"));
        assertTrue(receipt.contains("Price:"));
        assertTrue(receipt.contains(String.format("%f", 500.0f)));
    }

    /**
     * Дополнительный тест: удаление первого ингредиента из списка.
     * Проверяем, что после удаления остаётся ожидаемый элемент.
     */
    @Test
    public void testRemoveIngredientFirst() {
        burger.addIngredient(mockIng1);
        burger.addIngredient(mockIng2);
        burger.removeIngredient(0);
        assertEquals(1, burger.ingredients.size());
        assertEquals(mockIng2, burger.ingredients.get(0));
    }

    /**
     * Удаление последнего ингредиента из списка.
     * Проверяем, что остался первый элемент.
     */
    @Test
    public void testRemoveIngredientLast() {
        burger.addIngredient(mockIng1);
        burger.addIngredient(mockIng2);
        burger.removeIngredient(1);
        assertEquals(1, burger.ingredients.size());
        assertEquals(mockIng1, burger.ingredients.get(0));
    }
}