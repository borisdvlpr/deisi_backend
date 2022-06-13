package pt.ulusofona.deisi.landingPage.controller

import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.models.FileCreateRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.deisi.landingPage.dao.Teacher
import pt.ulusofona.deisi.landingPage.form.TeacherForm
import pt.ulusofona.deisi.landingPage.repository.TeacherRepository
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/backoffice/teachers")
class TeacherController(val teacherRepository: TeacherRepository) {

    @GetMapping(value = ["/list"])
    fun listTeachers(@RequestParam("age") age: Int?, model: ModelMap, principal: Principal?): String {
        val teachers = if (age == null) {
            teacherRepository.findAll()  // get all teachers from DB
        } else {
            model["age"] = age
            teacherRepository.findByAge(age)
        }
        model["teachers"] = teachers

        return "list-teachers"
    }

    @GetMapping(value = ["/new"])
    fun showTeachersForm(model: ModelMap): String {
        model["teacherForm"] = TeacherForm()
        return "new-teacher-form"
    }

    @GetMapping(value = ["/edit/{id}"])
    fun showTeacherForm(@PathVariable("id") id: Long, model: ModelMap): String {
        val teacherOptional = teacherRepository.findById(id)
        if (teacherOptional.isPresent) {
            val teacher = teacherOptional.get()
            model["teacherForm"] = TeacherForm(teacherId = teacher.id.toString(), name = teacher.name, age = teacher.age, imgSrc = teacher.imgSrc, description = teacher.description)
        }

        return "new-teacher-form"
    }

    @GetMapping(value = ["/delete/{id}"])
    fun showTeacherDelete(@PathVariable("id") id: Long, model: ModelMap): String {
        val teacherOptional = teacherRepository.findById(id)
        if (teacherOptional.isPresent) {
            val teacher = teacherOptional.get()
            model["teacherForm"] = TeacherForm(teacherId = teacher.id.toString(), name = teacher.name, age = teacher.age, imgSrc = teacher.imgSrc, description = teacher.description)
        }

        return "delete-teacher-form"
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateTeacher(@Valid @ModelAttribute("teacherForm") teacherForm: TeacherForm,
                                  @RequestParam("imgFile") file: MultipartFile,
                                  bindingResult: BindingResult,
                                  redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-teacher-form"
        }

        if(teacherForm.teacherId.isNullOrBlank() || !file.isEmpty) {
            try {
                uploadImageCDN(teacherForm, file)

            } catch (err: Exception) {
                redirectAttributes.addFlashAttribute("message", "Erro submissão foto professor: $err")
                return "redirect:/backoffice/teachers/list"
            }
        }

        val teacher: Teacher =
            if (teacherForm.teacherId.isNullOrBlank()) {
                Teacher(name = teacherForm.name!!, age = teacherForm.age!!, imgSrc = teacherForm.imgSrc!!, description = teacherForm.description!!)
            } else {
                val newSrc: String;
                val t = teacherRepository.findById(teacherForm.teacherId!!.toLong()).get()

                newSrc = if(!teacherForm.imgSrc.equals(null)) { teacherForm.imgSrc.toString(); } else { t.imgSrc }

                t.name = teacherForm.name!!
                t.age = teacherForm.age!!
                t.imgSrc = newSrc
                t.description = teacherForm.description!!
                t
            }

        teacherRepository.save(teacher)

        if (teacherForm.teacherId == null) {
            redirectAttributes.addFlashAttribute("message", "Professor inserido com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Professor editado com sucesso")
        }
        return "redirect:/backoffice/teachers/list"
    }

    @PostMapping(value = ["/delete"])
    fun deleteTeacher(@Valid @ModelAttribute("teacherForm") teacherForm: TeacherForm,
                          bindingResult: BindingResult,
                          redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) {
            return "delete-teacher-form"
        }

        val teacher: Teacher =
            if (teacherForm.teacherId.isNullOrBlank()) {
                Teacher(name = teacherForm.name!!, age = teacherForm.age!!, imgSrc = teacherForm.imgSrc!!, description = teacherForm.description!!)
            } else {
                val t = teacherRepository.findById(teacherForm.teacherId!!.toLong()).get()
                t.name = teacherForm.name!!
                t.age = teacherForm.age!!
                t.imgSrc = teacherForm.imgSrc!!
                t.description = teacherForm.description!!
                t
            }

        teacherRepository.delete(teacher)
        redirectAttributes.addFlashAttribute("message", "Professor eliminado com sucesso")

        return "redirect:/backoffice/teachers/list"
    }

    fun uploadImageCDN(teacherForm: TeacherForm, file: MultipartFile) {
        val fileName: String = file.originalFilename ?: "temp"
        val tempDir = Files.createTempDirectory("landing-page-uploads").toFile()

        val newImage = File(tempDir, fileName)
        file.transferTo(newImage)

        val bytes = Files.readAllBytes(Paths.get(newImage.path))
        val fileCreateRequest = FileCreateRequest(bytes, fileName)
        fileCreateRequest.isUseUniqueFileName = false
        val result = ImageKit.getInstance().upload(fileCreateRequest)

        teacherForm.imgSrc = result.url
        newImage.delete()
    }
}
