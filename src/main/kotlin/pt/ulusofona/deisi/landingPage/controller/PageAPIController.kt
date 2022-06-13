package pt.ulusofona.deisi.landingPage.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import pt.ulusofona.deisi.landingPage.dao.Page
import pt.ulusofona.deisi.landingPage.form.PageForm
import pt.ulusofona.deisi.landingPage.repository.PageRepository
import javax.validation.Valid


@RestController
@RequestMapping("/api/pages")
class PageAPIController(val pageRepository: PageRepository) {

    @GetMapping(value = ["/list"])
    fun listPages(@RequestParam("pageName") pageName: String?): List<Page> {
        val pages = if (pageName == null) {
            pageRepository.findAll()
        } else {
            pageRepository.findByPageName(pageName)
        }
        return pages
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdatePage(@Valid @RequestBody pageForm: PageForm,
                           bindingResult: BindingResult) : ResponseEntity<PageForm> {

        if (bindingResult.hasErrors()) {
            return ResponseEntity(pageForm, HttpStatus.FORBIDDEN)
        }

        val page: Page =
            if (pageForm.pageId.isNullOrBlank()) {
                Page(pageName = pageForm.pageName!!, title = pageForm.title!!, stat = pageForm.stat!!, statText = pageForm.statText!!, pageText = pageForm.pageText!!)
            } else {
                val p = pageRepository.findById(pageForm.pageId!!.toLong()).get()
                p.pageName = pageForm.pageName!!
                p.title = pageForm.title!!
                p.stat = pageForm.stat!!
                p.statText = pageForm.statText!!
                p.pageText = pageForm.pageText!!
                p
            }

        pageRepository.save(page)

        if (pageForm.pageId == null) {
            return ResponseEntity(pageForm, HttpStatus.CREATED)
        } else {
            return ResponseEntity(pageForm, HttpStatus.OK)
        }
    }
}
