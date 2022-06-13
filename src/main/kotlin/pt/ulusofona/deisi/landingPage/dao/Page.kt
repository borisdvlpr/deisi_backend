package pt.ulusofona.deisi.landingPage.dao

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Page (
        @Id @GeneratedValue
        val id: Long = 0,
        var pageName: String,
        var title: String,
        var stat: String,
        var statText: String,
        @Column(length = 510)
        var pageText: String)
