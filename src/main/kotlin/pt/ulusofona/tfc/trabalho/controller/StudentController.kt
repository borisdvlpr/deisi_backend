package pt.ulusofona.tfc.trabalho.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Student
import pt.ulusofona.tfc.trabalho.form.StudentForm
import pt.ulusofona.tfc.trabalho.repository.StudentRepository
import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.models.FileCreateRequest
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
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
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            println(bindingResult)
            return "new-student-form"
        }

        if(studentForm.studentId.isNullOrBlank()) {
            try {
                val newFile = studentForm.imgFile
                println("$newFile - new file")
                newFile?.copyTo(File("../tmp/${newFile.name}"))
                val filePath: String? = newFile?.absolutePath

                // para testar a submissão no imagekit:
                //      - comentar as linhas anteriores
                //      - na criação da variável path abaixo, por a path do ficheiro onde se encontra a variável $filePath
                //      - na criação da variável fileCreateRequest, por o nome do ficheiro onde se encontra a variável newFile?.name
                //
                // na classe StudentForm foi retirada a obrigatoriedade de preencher o campo imgSrc

                val bytes = Files.readAllBytes(Paths.get("$filePath"))
                val fileCreateRequest = FileCreateRequest(bytes, newFile?.name)
                fileCreateRequest.isUseUniqueFileName = false
                val result = ImageKit.getInstance().upload(fileCreateRequest)
                studentForm.imgSrc = result.url

            } catch (err: Error) {
                redirectAttributes.addFlashAttribute("message", "Erro submissão foto aluno: $err")
            }
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
}
