package pt.ulusofona.deisi.landingPage.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.deisi.landingPage.dao.Page
import pt.ulusofona.deisi.landingPage.form.PageForm
import pt.ulusofona.deisi.landingPage.repository.PageRepository
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/backoffice/pages")
class PageController(val pageRepository: PageRepository) {

    @GetMapping(value = ["/list"])
    fun listPages(@RequestParam("id") pageName: String?, model: ModelMap, principal: Principal?): String {
        val pages = if (pageName == null) {
            pageRepository.findAll()
        } else {
            model["pageName"] = pageName
            pageRepository.findByPageName(pageName)
        }
        model["pages"] = pages

        return "list-pages"
    }

    @GetMapping(value = ["/new"])
    fun showPageForm(model: ModelMap): String {
        model["pageForm"] = PageForm()
        return "new-page-form"
    }

    @GetMapping(value = ["/edit/{id}"])
    fun showPageForm(@PathVariable("id") id: Long, model: ModelMap): String {
        val pageOptional = pageRepository.findById(id)
        if (pageOptional.isPresent) {
            val page = pageOptional.get()
            model["pageForm"] = PageForm(pageId = page.id.toString(), pageName = page.pageName, title = page.title, stat = page.stat, statText = page.statText, pageText = page.pageText)
        }

        return "new-page-form"
    }

    @GetMapping(value = ["/delete/{id}"])
    fun showPageDelete(@PathVariable("id") id: Long, model: ModelMap): String {
        val pageOptional = pageRepository.findById(id)
        if (pageOptional.isPresent) {
            val page = pageOptional.get()
            model["pageForm"] = PageForm(pageId = page.id.toString(), pageName = page.pageName, title = page.title, stat = page.stat, statText = page.statText, pageText = page.pageText)
        }

        return "delete-page-form"
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdatePage(@Valid @ModelAttribute("pageForm") pageForm: PageForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-page-form"
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
            redirectAttributes.addFlashAttribute("message", "Página inserida com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Página editada com sucesso")
        }
        return "redirect:/backoffice/pages/list"
    }

    @PostMapping(value = ["/delete"])
    fun deletePage(@Valid @ModelAttribute("pageForm") pageForm: PageForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) {
            return "delete-page-form"
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

        pageRepository.delete(page)
        redirectAttributes.addFlashAttribute("message", "Página eliminada com sucesso")

        return "redirect:/backoffice/pages/list"
    }
}
