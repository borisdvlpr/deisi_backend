package pt.ulusofona.deisi.landingPage.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.deisi.landingPage.dao.Student

interface StudentRepository: JpaRepository<Student, Long> {

    fun findByGradYear(gradYear: Int): List<Student>
}