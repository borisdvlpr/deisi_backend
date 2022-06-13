package pt.ulusofona.deisi.landingPage.dao

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Company (
        @Id @GeneratedValue
        val id: Long = 0,
        var name: String,
        var imgSrc: String,
        var description: String)
