package pt.ulusofona.tfc.trabalho.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Teacher
import pt.ulusofona.tfc.trabalho.form.TeacherForm
import pt.ulusofona.tfc.trabalho.repository.TeacherRepository
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/teachers")
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
            model["studentForm"] = TeacherForm(teacherId = teacher.id.toString(), name = teacher.name, age = teacher.age)
        }

        return "new-teacher-form"
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateTeacher(@Valid @ModelAttribute("studentForm") teacherForm: TeacherForm,
                              bindingResult: BindingResult,
                              redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-teacher-form"
        }

        val student: Teacher =
            if (teacherForm.teacherId.isNullOrBlank()) {  // new teacher
                Teacher(name = teacherForm.name!!, age = teacherForm.age!!)
            } else { // edit student
                val t = teacherRepository.findById(teacherForm.teacherId!!.toLong()).get()
                t.name = teacherForm.name!!
                t.age = teacherForm.age!!
                t
            }

        teacherRepository.save(student)

        if (teacherForm.teacherId == null) {
            redirectAttributes.addFlashAttribute("message", "Professor inserido com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Professor editado com sucesso")
        }
        return "redirect:/teachers/list"
    }
}