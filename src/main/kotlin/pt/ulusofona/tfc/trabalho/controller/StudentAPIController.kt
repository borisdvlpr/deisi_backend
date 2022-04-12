package pt.ulusofona.tfc.trabalho.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Student
import pt.ulusofona.tfc.trabalho.form.StudentForm
import pt.ulusofona.tfc.trabalho.repository.StudentRepository
import javax.validation.Valid


@RestController
@RequestMapping("/students-api")
class StudentAPIController(val studentRepository: StudentRepository) {

    @GetMapping(value = ["/list"])
    fun listStudents(@RequestParam("age") age: Int?): List<Student> {
        val students = if (age == null) {
            studentRepository.findAll()
        } else {
            studentRepository.findByAge(age)
        }
        return students
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateStudent(@Valid @RequestBody studentForm: StudentForm,
                           bindingResult: BindingResult) : ResponseEntity<StudentForm> {

        if (bindingResult.hasErrors()) {
            return ResponseEntity(studentForm, HttpStatus.FORBIDDEN)
        }

        val student: Student =
            if (studentForm.studentId.isNullOrBlank()) {  // new student
                Student(name = studentForm.name!!, age = studentForm.age!!, imgSrc = studentForm.imgSrc!!, description = studentForm.description!!)
            } else { // edit student
                val s = studentRepository.findById(studentForm.studentId!!.toLong()).get()
                s.name = studentForm.name!!
                s.age = studentForm.age!!
                s.imgSrc = studentForm.imgSrc!!
                s.description = studentForm.description!!
                s
            }

        studentRepository.save(student)

        if (studentForm.studentId == null) {
            return ResponseEntity(studentForm, HttpStatus.CREATED)
        } else {
            return ResponseEntity(studentForm, HttpStatus.OK)
        }
    }
}