package truck.project.truck_guia.domain.vo

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Placa private constructor(val value: String){
    init {
        require(value.length in 7..8){
            "La placa debe tener entre 7 y 8 caracteres"
        }
        require(value.first().isLetter()){
            "La primera letra de la placa debe ser una letra"
        }
        require(value.last().isDigit()){
            "La última letra de la placa debe ser un número"
        }
        require(value.takeLast(4).all { it.isDigit() }){
            "Los últimos 4 caracteres deben ser números"
        }
    }
    companion object {
        fun create(value: String) = Placa(value.trim().uppercase())
    }
    override fun toString(): String {
        return value
    }
}

