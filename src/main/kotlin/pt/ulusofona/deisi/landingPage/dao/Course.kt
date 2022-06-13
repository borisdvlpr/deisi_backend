package pt.ulusofona.deisi.landingPage.dao

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Course (
        @Id @GeneratedValue
        val id: Long = 0,
        var title: String,
        var degree: String,
        var url: String,
        @Column(length = 1000)
        var courseDescription: String)
