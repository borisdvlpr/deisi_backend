package pt.ulusofona.deisi.landingPage.controller

import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.models.FileCreateRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.deisi.landingPage.dao.Student
import pt.ulusofona.deisi.landingPage.form.StudentForm
import pt.ulusofona.deisi.landingPage.repository.StudentRepository
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/backoffice/students")
class StudentController(val studentRepository: StudentRepository) {

    @GetMapping(value = ["/list"])
    fun listStudents(@RequestParam("gradYear") gradYear: Int?, model: ModelMap, principal: Principal?): String {
        val students = if (gradYear == null) {
            studentRepository.findAll()  // get all students from DB
        } else {
            model["gradYear"] = gradYear
            studentRepository.findByGradYear(gradYear)
        }
        model["students"] = students

        return "list-students"
    }

    @GetMapping(value = ["/new"])
    fun showStudentForm(model: ModelMap): String {
        model["studentForm"] = StudentForm()
        return "new-student-form"
    }

    @GetMapping(value = ["/edit/{id}"])
    fun showStudentForm(@PathVariable("id") id: Long, model: ModelMap): String {
        val studentOptional = studentRepository.findById(id)
        if (studentOptional.isPresent) {
            val student = studentOptional.get()
            model["studentForm"] = StudentForm(studentId = student.id.toString(), name = student.name, gradYear = student.gradYear, imgSrc = student.imgSrc, description = student.description)
        }

        return "new-student-form"
    }

    @GetMapping(value = ["/delete/{id}"])
    fun showStudentDelete(@PathVariable("id") id: Long, model: ModelMap): String {
        val studentOptional = studentRepository.findById(id)
        if (studentOptional.isPresent) {
            val student = studentOptional.get()
            model["studentForm"] = StudentForm(studentId = student.id.toString(), name = student.name, gradYear = student.gradYear, imgSrc = student.imgSrc, description = student.description)
        }

        return "delete-student-form"
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateStudent(@Valid @ModelAttribute("studentForm") studentForm: StudentForm,
                               @RequestParam("imgFile") file: MultipartFile,
                               bindingResult: BindingResult,
                               redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-student-form"
        }

        if(studentForm.studentId.isNullOrBlank() || !file.isEmpty) {
            try {
                uploadImageCDN(studentForm, file)

            } catch (err: Exception) {
                redirectAttributes.addFlashAttribute("message", "Erro submissão foto aluno: $err")
                return "redirect:/backoffice/students/list"
            }
        }

        val student: Student =
            if (studentForm.studentId.isNullOrBlank()) {
                Student(name = studentForm.name!!, gradYear = studentForm.gradYear!!, imgSrc = studentForm.imgSrc!!, description = studentForm.description!!)
            } else {
                val newSrc: String;
                val s = studentRepository.findById(studentForm.studentId!!.toLong()).get()

                newSrc = if(!studentForm.imgSrc.equals(null)) { studentForm.imgSrc.toString(); } else { s.imgSrc }

                s.name = studentForm.name!!
                s.gradYear = studentForm.gradYear!!
                s.imgSrc = newSrc
                s.description = studentForm.description!!
                s
            }

        studentRepository.save(student)

        if (studentForm.studentId == null) {
            redirectAttributes.addFlashAttribute("message", "Aluno inserido com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Aluno editado com sucesso")
        }

        return "redirect:/backoffice/students/list"
    }

    @PostMapping(value = ["/delete"])
    fun deleteStudent(@Valid @ModelAttribute("studentForm") studentForm: StudentForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) {
            return "delete-student-form"
        }

        val student: Student =
            if (studentForm.studentId.isNullOrBlank()) {
                Student(name = studentForm.name!!, gradYear = studentForm.gradYear!!, imgSrc = studentForm.imgSrc!!, description = studentForm.description!!)
            } else {
                val s = studentRepository.findById(studentForm.studentId!!.toLong()).get()
                s.name = studentForm.name!!
                s.gradYear = studentForm.gradYear!!
                s.imgSrc = studentForm.imgSrc!!
                s.description = studentForm.description!!
                s
            }

        studentRepository.delete(student)
        redirectAttributes.addFlashAttribute("message", "Aluno eliminado com sucesso")

        return "redirect:/backoffice/students/list"
    }

    fun uploadImageCDN(studentForm: StudentForm, file: MultipartFile) {
        val fileName: String = file.originalFilename ?: "temp"
        val tempDir = Files.createTempDirectory("landing-page-uploads").toFile()

        val newImage = File(tempDir, fileName)
        file.transferTo(newImage)

        val bytes = Files.readAllBytes(Paths.get(newImage.path))
        val fileCreateRequest = FileCreateRequest(bytes, fileName)
        fileCreateRequest.isUseUniqueFileName = false
        val result = ImageKit.getInstance().upload(fileCreateRequest)

        studentForm.imgSrc = result.url
        newImage.delete()
    }
}
