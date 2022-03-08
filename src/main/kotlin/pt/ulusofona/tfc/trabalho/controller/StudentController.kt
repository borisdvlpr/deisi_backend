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
            model["studentForm"] = StudentForm(studentId = student.id.toString(), name = student.name, age = student.age)
        }

        return "new-student-form"
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
            if (studentForm.studentId.isNullOrBlank()) {  // new student
                Student(name = studentForm.name!!, age = studentForm.age!!)
            } else { // edit student
                val s = studentRepository.findById(studentForm.studentId!!.toLong()).get()
                s.name = studentForm.name!!
                s.age = studentForm.age!!
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
}