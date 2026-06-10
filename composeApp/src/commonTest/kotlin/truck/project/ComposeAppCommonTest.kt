package truck.project

import truck.project.truck_guia.domain.vo.Placa
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ComposeAppCommonTest {

    @Test
    fun `Test Placa`() {
        val input = " abc-1234"
        val expect = "ABC-1234"
        val placa = Placa.create(input)
        assertEquals(expect, placa.value)
    }

    @Test
    fun `Placa debe eliminar espacios al inicio y al final`() {
        // Arrange
        val input = "   kmp-2026   "
        val expect = "KMP-2026"

        // Act
        val placa = Placa.create(input)

        // Assert
        assertEquals(expect, placa.value)
    }

    @Test
    fun `Placa debe convertir letras minusculas a mayusculas`() {
        // Arrange
        val input = "abc-5678"
        val expect = "ABC-5678"

        // Act
        val placa = Placa.create(input)

        // Assert
        assertEquals(expect, placa.value)
    }

    @Test
    fun `Placa no debe aceptar texto vacio`() {
        // Arrange
        val input = ""

        // Act - Assert
        assertFailsWith<IllegalArgumentException> {
            Placa.create(input)
        }
    }

    @Test
    fun `Placa no debe aceptar solo espacios`() {
        // Arrange
        val input = "     "

        // Act - Assert
        assertFailsWith<IllegalArgumentException> {
            Placa.create(input)
        }
    }
}