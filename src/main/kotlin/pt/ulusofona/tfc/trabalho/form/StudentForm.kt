package pt.ulusofona.tfc.trabalho.form

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


data class StudentForm (
        var studentId: String? = null,

        @field:NotEmpty(message = "Erro: O nome tem que estar preenchido")
        var name: String? = null,

        @field:NotNull(message = "Erro: A idade tem que estar preenchida")
        @field:Min(value=18, message = "Erro: A idade tem que ser >= 18")
        var age: Int? = null,

        @field:NotEmpty(message = "Erro: é necessário um link para a imagem")
        var imgSrc: String? = null,

        @field:NotEmpty(message = "Erro: a descrição tem que ser preenchida")
        @field:Length(min=9, max=260, message = "Erro: descrição longa (max. 200 caracteres)")
        var description: String? = null
)