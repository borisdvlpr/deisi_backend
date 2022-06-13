package pt.ulusofona.deisi.landingPage.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import pt.ulusofona.deisi.landingPage.dao.Student
import pt.ulusofona.deisi.landingPage.form.StudentForm
import pt.ulusofona.deisi.landingPage.repository.StudentRepository
import javax.validation.Valid


@RestController
@RequestMapping("/api/students")
class StudentAPIController(val studentRepository: StudentRepository) {

    @GetMapping(value = ["/list"])
    fun listStudents(@RequestParam("gradYear") gradYear: Int?): List<Student> {
        val students = if (gradYear == null) {
            studentRepository.findAll()
        } else {
            studentRepository.findByGradYear(gradYear)
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
                Student(name = studentForm.name!!, gradYear = studentForm.gradYear!!, imgSrc = studentForm.imgSrc!!, description = studentForm.description!!)
            } else { // edit student
                val s = studentRepository.findById(studentForm.studentId!!.toLong()).get()
                s.name = studentForm.name!!
                s.gradYear = studentForm.gradYear!!
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