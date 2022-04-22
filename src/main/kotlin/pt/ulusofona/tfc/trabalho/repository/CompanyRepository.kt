package pt.ulusofona.tfc.trabalho.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.tfc.trabalho.dao.Company


interface CompanyRepository: JpaRepository<Company, Long> {

    fun findByName(name: String): List<Company>
}