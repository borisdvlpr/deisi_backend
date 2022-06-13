package pt.ulusofona.deisi.landingPage.form

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty


data class PageForm (
        var pageId: String? = null,

        @field:NotEmpty(message = "Erro: O nome da página tem que estar preenchido")
        var pageName: String? = null,

        @field:NotEmpty(message = "Erro: O título tem que estar preenchido")
        var title: String? = null,

        @field:NotEmpty(message = "Erro: A estatística tem que estar preenchida")
        var stat: String? = null,

        @field:NotEmpty(message = "Erro: O texto da estatística tem que estar preenchida")
        var statText: String? = null,

        @field:NotEmpty(message = "Erro: A descrição tem que estar preenchida")
        @field:Length(min=9, max=510, message = "Erro: tamanho da descrição incompatível (max. 500 caracteres)")
        var pageText: String? = null
)
