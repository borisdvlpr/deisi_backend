package pt.ulusofona.tfc.trabalho.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.Course
import pt.ulusofona.tfc.trabalho.form.CourseForm
import pt.ulusofona.tfc.trabalho.repository.CourseRepository
import java.security.Principal
import javax.validation.Valid


@Controller
@RequestMapping("/courses")
class CourseController(val courseRepository: CourseRepository) {

    @GetMapping(value = ["/list"])
    fun listCourses(@RequestParam("id") title: String?, model: ModelMap, principal: Principal?): String {
        val courses = if (title == null) {
            courseRepository.findAll()
        } else {
            model["title"] = title
            courseRepository.findByTitle(title)
        }
        model["courses"] = courses

        return "list-courses"
    }

    @GetMapping(value = ["/new"])
    fun showCourseForm(model: ModelMap): String {
        model["courseForm"] = CourseForm()
        return "new-course-form"
    }

    @GetMapping(value = ["/edit/{id}"])
    fun showCourseForm(@PathVariable("id") id: Long, model: ModelMap): String {
        val courseOptional = courseRepository.findById(id)
        if (courseOptional.isPresent) {
            val course = courseOptional.get()
            model["courseForm"] = CourseForm(courseId = course.id.toString(), title = course.title, degree = course.degree, url = course.url, courseDescription = course.courseDescription)
        }

        return "new-course-form"
    }

    @GetMapping(value = ["/delete/{id}"])
    fun showCourseDelete(@PathVariable("id") id: Long, model: ModelMap): String {
        val courseOptional = courseRepository.findById(id)
        if (courseOptional.isPresent) {
            val course = courseOptional.get()
            model["courseForm"] = CourseForm(courseId = course.id.toString(), title = course.title, degree = course.degree, url = course.url, courseDescription = course.courseDescription)
        }

        return "delete-course-form"
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateCourse(@Valid @ModelAttribute("courseForm") courseForm: CourseForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes
    ) : String {

        if (bindingResult.hasErrors()) {
            return "new-course-form"
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
            redirectAttributes.addFlashAttribute("message", "Curso inserido com sucesso")
        } else {
            redirectAttributes.addFlashAttribute("message", "Curso editado com sucesso")
        }
        return "redirect:/courses/list"
    }

    @PostMapping(value = ["/delete"])
    fun deleteCourse(@Valid @ModelAttribute("courseForm") courseForm: CourseForm,
                           bindingResult: BindingResult,
                           redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) {
            return "delete-course-form"
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

        courseRepository.delete(course)
        redirectAttributes.addFlashAttribute("message", "Curso eliminado com sucesso")

        return "redirect:/courses/list"
    }
}
