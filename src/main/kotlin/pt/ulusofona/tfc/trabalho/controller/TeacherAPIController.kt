package pt.ulusofona.tfc.trabalho.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Teacher
import pt.ulusofona.tfc.trabalho.form.TeacherForm
import pt.ulusofona.tfc.trabalho.repository.TeacherRepository
import javax.validation.Valid


@RestController
@RequestMapping("/teachers-api")
class TeacherAPIController(val teacherRepository: TeacherRepository) {

    @GetMapping(value = ["/list"])
    fun listTeachers(@RequestParam("age") age: Int?): List<Teacher> {
        val teachers = if (age == null) {
            teacherRepository.findAll()  // get all teachers from DB
        } else {
            teacherRepository.findByAge(age)
        }
        return teachers
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateTeachers(@Valid @RequestBody teacherForm: TeacherForm,
                          bindingResult: BindingResult) : ResponseEntity<TeacherForm> {

        if (bindingResult.hasErrors()) {
            return ResponseEntity(teacherForm, HttpStatus.FORBIDDEN)
        }

        val student: Teacher =
            if (teacherForm.teacherId.isNullOrBlank()) {  // new teacher
                Teacher(name = teacherForm.name!!, age = teacherForm.age!!, imgSrc = teacherForm.imgSrc!!, description = teacherForm.description!!)
            } else { // edit teacher
                val t = teacherRepository.findById(teacherForm.teacherId!!.toLong()).get()
                t.name = teacherForm.name!!
                t.age = teacherForm.age!!
                t.imgSrc = teacherForm.imgSrc!!
                t.description = teacherForm.description!!
                t
            }

        teacherRepository.save(student)

        if (teacherForm.teacherId == null) {
            return ResponseEntity(teacherForm, HttpStatus.CREATED)
        } else {
            return ResponseEntity(teacherForm, HttpStatus.OK)
        }
    }
}