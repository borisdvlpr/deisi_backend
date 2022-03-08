package pt.ulusofona.tfc.trabalho.form

import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


data class TeacherForm (
        var teacherId: String? = null,

        @field:NotEmpty(message = "Erro: O nome tem que estar preenchido")
        var name: String? = null,

        @field:NotNull(message = "Erro: A idade tem que estar preenchida")
        @field:Min(value=18, message = "Erro: A idade tem que ser >= 18")
        var age: Int? = null
)