package pt.ulusofona.tfc.trabalho.form

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty


data class CompanyForm (
        var companyId: String? = null,

        @field:NotEmpty(message = "Erro: O nome tem que estar preenchido")
        var name: String? = null,

        @field:NotEmpty(message = "Erro: é necessário um link para a imagem")
        var imgSrc: String? = null,

        @field:NotEmpty(message = "Erro: a descrição tem que ser preenchida")
        @field:Length(min=9, max=200, message = "Erro: descrição longa (max. 200 caracteres)")
        var description: String? = null
)