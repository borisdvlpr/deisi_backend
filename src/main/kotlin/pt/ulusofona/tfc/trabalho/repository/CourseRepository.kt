package pt.ulusofona.tfc.trabalho.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.tfc.trabalho.dao.Course

interface CourseRepository: JpaRepository<Course, Long> {

    fun findByTitle(title: String): List<Course>
}