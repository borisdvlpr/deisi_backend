package pt.ulusofona.deisi.landingPage.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.deisi.landingPage.dao.Company


interface CompanyRepository: JpaRepository<Company, Long> {

    fun findByName(name: String): List<Company>
}