package ru.yandex.praktikum9;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Юнит-тесты для класса Bun (булочка).
 */
public class BunTest {

    /**
     * Проверяет, что правильно инициализируются поля,
     * а getName() и getPrice() возвращают сохранённые значения.
     */
    @Test
    public void testBunConstructorAndGetters() {
        // Создаём тестовый объект булочки с конкретными данными
        Bun bun = new Bun("black bun", 100.0f);

        // Проверяем, что название сохранилось корректно
        assertEquals("black bun", bun.getName());

        // Проверяем, что цена сохранилась корректно
        assertEquals(100.0f, bun.getPrice(), 0.001f);
    }
}
