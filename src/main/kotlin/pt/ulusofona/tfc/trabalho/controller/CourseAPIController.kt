package pt.ulusofona.tfc.trabalho.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Course
import pt.ulusofona.tfc.trabalho.form.CourseForm
import pt.ulusofona.tfc.trabalho.repository.CourseRepository
import javax.validation.Valid


@RestController
@RequestMapping("/courses-api")
class CourseAPIController(val courseRepository: CourseRepository) {

    @GetMapping(value = ["/list"])
    fun listCourses(@RequestParam("title") title: String?): List<Course> {
        val courses = if (title == null) {
            courseRepository.findAll()
        } else {
            courseRepository.findByTitle(title)
        }
        return courses
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateCourse(@Valid @RequestBody courseForm: CourseForm,
                           bindingResult: BindingResult) : ResponseEntity<CourseForm> {

        if (bindingResult.hasErrors()) {
            return ResponseEntity(courseForm, HttpStatus.FORBIDDEN)
        }

        val course: Course =
            if (courseForm.courseId.isNullOrBlank()) {
                Course(title = courseForm.title!!, degree = courseForm.degree!!, url = courseForm.url!!, courseDescription = courseForm.courseDescription!!)
            } else {
                val c = courseRepository.findById(courseForm.courseId!!.toLong()).get()
                c.title = courseForm.title!!
                c.degree = courseForm.degree!!
                c.url = courseForm.url!!
                c.courseDescription = courseForm.courseDescription!!
                c
            }

        courseRepository.save(course)

        if (courseForm.courseId == null) {
            return ResponseEntity(courseForm, HttpStatus.CREATED)
        } else {
            return ResponseEntity(courseForm, HttpStatus.OK)
        }
    }
}