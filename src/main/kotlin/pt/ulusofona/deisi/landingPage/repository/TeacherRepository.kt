package pt.ulusofona.deisi.landingPage.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.deisi.landingPage.dao.Teacher

interface TeacherRepository: JpaRepository<Teacher, Long> {

    fun findByAge(age: Int): List<Teacher>
}