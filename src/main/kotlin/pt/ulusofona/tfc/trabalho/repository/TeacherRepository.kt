package pt.ulusofona.tfc.trabalho.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.tfc.trabalho.dao.Teacher

interface TeacherRepository: JpaRepository<Teacher, Long> {

    fun findByAge(age: Int): List<Teacher>
}