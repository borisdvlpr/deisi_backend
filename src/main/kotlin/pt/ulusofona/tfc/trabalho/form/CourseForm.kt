package pt.ulusofona.tfc.trabalho.form

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


data class CourseForm (
        var courseId: String? = null,

        @field:NotEmpty(message = "Erro: O título tem que estar preenchido")
        var title: String? = null,

        @field:NotEmpty(message = "Erro: O grau tem que estar preenchida")
        var degree: String? = null,

        @field:NotEmpty(message = "Erro: O estatuto do aluno tem que estar preenchida")
        var studentDegree: String? = null,

        @field:NotEmpty(message = "Erro: O tempo tem que estar preenchida")
        var time: String? = null,

        @field:NotNull(message = "Erro: O tempo tem que estar preenchida")
        var ects: Int? = null,

        @field:NotEmpty(message = "Erro: O nome do diretor tem que estar preenchido")
        var directorName: String? = null,

        @field:NotEmpty(message = "Erro: O contacto do diretor tem que estar preenchido")
        var directorContact: String? = null,

        @field:NotEmpty(message = "Erro: o link do curso tem que estar preenchido")
        var url: String? = null
)