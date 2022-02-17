package pt.ulusofona.tfc.trabalho.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.tfc.trabalho.dao.User
import pt.ulusofona.tfc.trabalho.form.UserForm
import pt.ulusofona.tfc.trabalho.repository.UserRepository
import javax.validation.Valid


@RestController
@RequestMapping("/users-api")
class UsersAPIController(val userRepository: UserRepository) {

    @GetMapping(value = ["/list"])
    fun listUsers(@RequestParam("age") age: Int?): List<User> {
        val users = if (age == null) {
            userRepository.findAll()  // get all users from DB
        } else {
            userRepository.findByAge(age)
        }
        return users
    }

    @PostMapping(value = ["/new"])
    fun createOrUpdateUser(@Valid @RequestBody userForm: UserForm,
                            bindingResult: BindingResult) : ResponseEntity<UserForm> {

        if (bindingResult.hasErrors()) {
            return ResponseEntity(userForm, HttpStatus.FORBIDDEN)
        }

        val user: User =
                if (userForm.userId.isNullOrBlank()) {  // new user
                    User(name = userForm.name!!, age = userForm.age!!)
                } else { // edit user
                    val u = userRepository.findById(userForm.userId!!.toLong()).get()
                    u.name = userForm.name!!
                    u.age = userForm.age!!
                    u
                }

        userRepository.save(user)

        if (userForm.userId == null) {
            return ResponseEntity(userForm, HttpStatus.CREATED)
        } else {
            return ResponseEntity(userForm, HttpStatus.OK)
        }
    }
}