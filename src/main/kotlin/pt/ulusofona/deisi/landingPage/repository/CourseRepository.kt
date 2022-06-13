package pt.ulusofona.deisi.landingPage.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.deisi.landingPage.dao.Course

interface CourseRepository: JpaRepository<Course, Long> {

    fun findByTitle(title: String): List<Course>
}