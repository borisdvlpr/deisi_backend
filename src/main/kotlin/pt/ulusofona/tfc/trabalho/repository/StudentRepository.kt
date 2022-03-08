package pt.ulusofona.tfc.trabalho.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.tfc.trabalho.dao.Student

interface StudentRepository: JpaRepository<Student, Long> {

    fun findByAge(age: Int): List<Student>
}