package pt.ulusofona.deisi.landingPage.form

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty


data class CourseForm (
        var courseId: String? = null,

        @field:NotEmpty(message = "Erro: O título tem que estar preenchido")
        var title: String? = null,

        @field:NotEmpty(message = "Erro: O grau tem que estar preenchida")
        var degree: String? = null,

        @field:NotEmpty(message = "Erro: O link tem que estar preenchida")
        var url: String? = null,

        @field:NotEmpty(message = "Erro: A descrição tem que estar preenchida")
        @field:Length(min=9, max=500, message = "Erro: tamanho da descrição incompatível (max. 500 caracteres)")
        var courseDescription: String? = null
)