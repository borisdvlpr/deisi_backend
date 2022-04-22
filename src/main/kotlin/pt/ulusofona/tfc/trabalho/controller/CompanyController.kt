package pt.ulusofona.tfc.trabalho.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Company
import pt.ulusofona.tfc.trabalho.form.CompanyForm
import pt.ulusofona.tfc.trabalho.repository.CompanyRepository
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/companies")
class CompanyController(val companyRepository: CompanyRepository) {

    @GetMapping(value = ["/list"])
    fun listCompanies(@RequestParam("name") name: String?, model: ModelMap, principal: Principal?): String {
        val companies = if (name == null) {
            companyRepository.findAll()
        } else {
            model["name"] = name
            companyRepository.findByName(name)
        }
        model["companies"] = companies

        return "list-companies"
    }

    @GetMapping(value = ["/new"])
    fun showCompanyForm(model: ModelMap): String {
        model["companyForm"] = CompanyForm()
        return "new-company-form"
    }

    @GetMapping(value = ["/edit/{id}"])
    fun showCompanyForm(@PathVariable("id") id: Long, model: ModelMap): String {
        val companyOptional = companyRepository.findById(id)
        if (companyOptional.isPresent) {
            val company = companyOptional.get()
            model["companyForm"] = CompanyForm(companyId = company.id.toString(), name = company.name, imgSrc = company.imgSrc, description = company.description)
        }

        return "new-company-form"
    }

    @GetMapping(value = ["/delete/{id}"])
    fun showCompanyDelete(@PathVariable("id") id: Long, model: ModelMap): String {
        val companyOptional = companyRepository.findById(id)
        if (companyOptional.isPresent) {
            val company = companyOptional.get()
            model["companyForm"] = CompanyForm(companyId = company.id.toString(), name = company.name, imgSrc = company.imgSrc, description = company.description)
        }

        return "delete-company-form"
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateCompany(@Valid @ModelAttribute("companyForm") companyForm: CompanyForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-company-form"
        }

        val company: Company =
            if (companyForm.companyId.isNullOrBlank()) {
                Company(name = companyForm.name!!, imgSrc = companyForm.imgSrc!!, description = companyForm.description!!)
            } else {
                val cm = companyRepository.findById(companyForm.companyId!!.toLong()).get()
                cm.name = companyForm.name!!
                cm.imgSrc = companyForm.imgSrc!!
                cm.description = companyForm.description!!
                cm
            }

        companyRepository.save(company)

        if (companyForm.companyId == null) {
            redirectAttributes.addFlashAttribute("message", "Empresa inserida com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Empresa editada com sucesso")
        }
        return "redirect:/companies/list"
    }

    @PostMapping(value = ["/delete"])
    fun deleteCompany(@Valid @ModelAttribute("companyForm") companyForm: CompanyForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) {
            return "delete-company-form"
        }

        val company: Company =
            if (companyForm.companyId.isNullOrBlank()) {
                Company(name = companyForm.name!!, imgSrc = companyForm.imgSrc!!, description = companyForm.description!!)
            } else {
                val cm = companyRepository.findById(companyForm.companyId!!.toLong()).get()
                cm.name = companyForm.name!!
                cm.imgSrc = companyForm.imgSrc!!
                cm.description = companyForm.description!!
                cm
            }

        companyRepository.delete(company)
        redirectAttributes.addFlashAttribute("message", "Empresa eliminada com sucesso")

        return "redirect:/companies/list"
    }
}
