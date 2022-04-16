package pt.ulusofona.tfc.trabalho.dao

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Course (
        @Id @GeneratedValue
        val id: Long = 0,
        var title: String,
        var degree: String,
        var studentDegree: String,
        var time: String,
        var ects: Int,
        var directorName: String,
        var directorContact: String,
        var url: String)
