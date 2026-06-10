package truck.project

import truck.project.truck_guia.domain.vo.Placa
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

    @Test
    fun `Test Placa`() {
        val input = " abc-1234"
        val expect = "ABC-1234"
        val placa = Placa.create(input)
        assertEquals(expect, placa.value)
    }
}