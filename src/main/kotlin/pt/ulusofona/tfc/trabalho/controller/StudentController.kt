package pt.ulusofona.tfc.trabalho.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Student
import pt.ulusofona.tfc.trabalho.form.StudentForm
import pt.ulusofona.tfc.trabalho.repository.StudentRepository
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/students")
class StudentController(val studentRepository: StudentRepository) {

    @GetMapping(value = ["/list"])
    fun listStudents(@RequestParam("age") age: Int?, model: ModelMap, principal: Principal?): String {
        val students = if (age == null) {
            studentRepository.findAll()  // get all students from DB
        } else {
            model["age"] = age
            studentRepository.findByAge(age)
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
            model["studentForm"] = StudentForm(studentId = student.id.toString(), name = student.name, age = student.age, imgSrc = student.imgSrc, description = student.description)
        }

        return "new-student-form"
    }

    @GetMapping(value = ["/delete/{id}"])
    fun showStudentDelete(@PathVariable("id") id: Long, model: ModelMap): String {
        val studentOptional = studentRepository.findById(id)
        if (studentOptional.isPresent) {
            val student = studentOptional.get()
            model["studentForm"] = StudentForm(studentId = student.id.toString(), name = student.name, age = student.age, imgSrc = student.imgSrc, description = student.description)
        }

        return "delete-student-form"
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateStudent(@Valid @ModelAttribute("studentForm") studentForm: StudentForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-student-form"
        }

        val student: Student =
            if (studentForm.studentId.isNullOrBlank()) {
                Student(name = studentForm.name!!, age = studentForm.age!!, imgSrc = studentForm.imgSrc!!, description = studentForm.description!!)
            } else {
                val s = studentRepository.findById(studentForm.studentId!!.toLong()).get()
                s.name = studentForm.name!!
                s.age = studentForm.age!!
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
        return "redirect:/students/list"
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
                Student(name = studentForm.name!!, age = studentForm.age!!, imgSrc = studentForm.imgSrc!!, description = studentForm.description!!)
            } else {
                val s = studentRepository.findById(studentForm.studentId!!.toLong()).get()
                s.name = studentForm.name!!
                s.age = studentForm.age!!
                s.imgSrc = studentForm.imgSrc!!
                s.description = studentForm.description!!
                s
            }

        studentRepository.delete(student)
        redirectAttributes.addFlashAttribute("message", "Aluno eliminado com sucesso")

        return "redirect:/students/list"
    }
}
