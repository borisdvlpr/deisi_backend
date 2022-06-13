package pt.ulusofona.deisi.landingPage.form

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


data class TeacherForm (
        var teacherId: String? = null,

        @field:NotEmpty(message = "Erro: O nome tem que estar preenchido")
        var name: String? = null,

        @field:NotNull(message = "Erro: A idade tem que estar preenchida")
        @field:Min(value=18, message = "Erro: A idade tem que ser >= 18")
        var age: Int? = null,

        var imgSrc: String? = null,

        @field:NotEmpty(message = "Erro: a descrição tem que ser preenchida")
        @field:Length(min=9, max=200, message = "Erro: descrição fora de tamanho (min. 9 caracteres, max. 200 caracteres)")
        var description: String? = null
)