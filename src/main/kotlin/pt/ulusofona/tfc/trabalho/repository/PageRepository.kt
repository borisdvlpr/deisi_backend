package pt.ulusofona.tfc.trabalho.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.tfc.trabalho.dao.Page

interface PageRepository: JpaRepository<Page, Long> {

    fun findByPageName(pageName: String): List<Page>
}
