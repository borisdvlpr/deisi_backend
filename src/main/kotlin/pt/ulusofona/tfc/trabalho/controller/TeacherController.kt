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
                          bindingResult: BindingResult,
                          redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-teacher-form"
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

        teacherRepository.save(teacher)

        if (teacherForm.teacherId == null) {
            redirectAttributes.addFlashAttribute("message", "Professor inserido com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Professor editado com sucesso")
        }
        return "redirect:/teachers/list"
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

        return "redirect:/teachers/list"
    }
}
