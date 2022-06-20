package pt.ulusofona.deisi.landingPage.controller

import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.models.FileCreateRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.deisi.landingPage.dao.Company
import pt.ulusofona.deisi.landingPage.form.CompanyForm
import pt.ulusofona.deisi.landingPage.repository.CompanyRepository
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/backoffice/companies")
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
                                  @RequestParam("imgFile") file: MultipartFile,
                                  bindingResult: BindingResult,
                                  redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-company-form"
        }

        if(companyForm.companyId.isNullOrBlank() || !file.isEmpty) {
            try {
                uploadImageCDN(companyForm, file)

            } catch (err: Exception) {
                redirectAttributes.addFlashAttribute("message", "Erro submissão foto empresa: $err")
                return "redirect:/backoffice/companies/list"
            }
        }

        val company: Company =
            if (companyForm.companyId.isNullOrBlank()) {
                Company(name = companyForm.name!!, imgSrc = companyForm.imgSrc!!, description = companyForm.description!!)
            } else {
                val newSrc: String;
                val cm = companyRepository.findById(companyForm.companyId!!.toLong()).get()

                newSrc = if(!companyForm.imgSrc.equals(null)) { companyForm.imgSrc.toString(); } else { cm.imgSrc }

                cm.name = companyForm.name!!
                cm.imgSrc = newSrc
                cm.description = companyForm.description!!
                cm
            }

        companyRepository.save(company)

        if (companyForm.companyId == null) {
            redirectAttributes.addFlashAttribute("message", "Empresa inserida com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Empresa editada com sucesso")
        }
        return "redirect:/backoffice/companies/list"
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

        return "redirect:/backoffice/companies/list"
    }

    fun uploadImageCDN(companyForm: CompanyForm, file: MultipartFile) {
        val fileName: String = file.originalFilename ?: "temp"
        val tempDir = Files.createTempDirectory("landing-page-uploads").toFile()

        val newImage = File(tempDir, fileName)
        file.transferTo(newImage)

        val bytes = Files.readAllBytes(Paths.get(newImage.path))
        val fileCreateRequest = FileCreateRequest(bytes, fileName)
        fileCreateRequest.isUseUniqueFileName = false
        val result = ImageKit.getInstance().upload(fileCreateRequest)

        companyForm.imgSrc = result.url
        newImage.delete()
    }
}
