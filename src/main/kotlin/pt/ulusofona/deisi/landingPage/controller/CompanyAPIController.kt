package pt.ulusofona.deisi.landingPage.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import pt.ulusofona.deisi.landingPage.dao.Company
import pt.ulusofona.deisi.landingPage.form.CompanyForm
import pt.ulusofona.deisi.landingPage.repository.CompanyRepository
import javax.validation.Valid


@RestController
@RequestMapping("/api/companies")
class CompanyAPIController(val companyRepository: CompanyRepository) {

    @GetMapping(value = ["/list"])
    fun listCompanies(@RequestParam("name") name: String?): List<Company> {
        val companies = if (name == null) {
            companyRepository.findAll()
        } else {
            companyRepository.findByName(name)
        }
        return companies
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateCompany(@Valid @RequestBody companyForm: CompanyForm,
                           bindingResult: BindingResult) : ResponseEntity<CompanyForm> {

        if (bindingResult.hasErrors()) {
            return ResponseEntity(companyForm, HttpStatus.FORBIDDEN)
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
            return ResponseEntity(companyForm, HttpStatus.CREATED)
        } else {
            return ResponseEntity(companyForm, HttpStatus.OK)
        }
    }
}