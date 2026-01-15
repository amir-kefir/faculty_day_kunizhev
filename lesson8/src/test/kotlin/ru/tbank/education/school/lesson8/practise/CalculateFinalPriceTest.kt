package ru.tbank.education.school.lesson8.practise


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CalculateFinalPriceTest {

    @Test
    fun `test 1`() {
        val result = 1080.0
        assertEquals(result, calculateFinalPrice(1000.0,10,20))
    }

    @Test
    fun `test 2`() {
        val result = 1200.0
        assertEquals(result, calculateFinalPrice(1000.0,0,20))
    }

    @Test
    fun `test 3`() {
        val result = 900.0
        assertEquals(result, calculateFinalPrice(1000.0,10,0))
    }

    @Test
    fun `test 4`() {
        val result = 1000.0
        assertEquals(result, calculateFinalPrice(1000.0,0,0))
    }

    @Test
    fun `test 5`() {
        assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(-1000.0,10,20)
        }
    }

    @Test
    fun `test 6_1`() {
        assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0,-10,20)
        }
    }

    @Test
    fun `test 6_2`() {
        assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0,110,20)
        }
    }

    @Test
    fun `test 7_1`() {
        assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0,10,-20)
        }
    }

    @Test
    fun `test 7_2`() {
        assertThrows(IllegalArgumentException::class.java) {
            calculateFinalPrice(1000.0,10,40)
        }
    }
}

    /**
     *
     * Сценарии для тестирования:
     *
     * 1. Позитивные сценарии (happy path):
     *    - Обычный случай: basePrice = 1000, discount = 10%, tax = 20% → проверить корректность формулы.
     *    - Без скидки: discountPercent = 0 → итог = basePrice + налог.
     *    - Без налога: taxPercent = 0 → итог = basePrice минус скидка.
     *    - Без скидки и без налога: итог = basePrice.
     *
     * 2. Негативные сценарии (исключения):
     *    - Отрицательная цена: basePrice < 0 → IllegalArgumentException.
     *    - Скидка вне диапазона: discountPercent < 0 или > 100 → IllegalArgumentException.
     *    - Налог вне диапазона: taxPercent < 0 или > 30 → IllegalArgumentException.
     */
