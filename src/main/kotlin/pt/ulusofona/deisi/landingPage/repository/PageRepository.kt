package pt.ulusofona.deisi.landingPage.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.deisi.landingPage.dao.Page

interface PageRepository: JpaRepository<Page, Long> {

    fun findByPageName(pageName: String): List<Page>
}
