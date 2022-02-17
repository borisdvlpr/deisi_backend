package pt.ulusofona.tfc.trabalho.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pt.ulusofona.tfc.trabalho.dao.User

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations=["classpath:application-test.properties"])
@ActiveProfiles("test")
class TestUsersController {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun testInsertAndThenList() {

        // primeiro verifico que a lista vem vazia
        var mvcResult = mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk)
                .andReturn()

        var result = mvcResult.modelAndView?.model?.get("users") as List<User>
        assertNotNull(result)
        assertEquals(0, result.size)

        // insiro um utilizador
        mockMvc.perform(post("/users/new")
                .param("name", "User 1")
                .param("age", "30"))
                .andExpect(status().isFound)  // redirect
                .andExpect(redirectedUrl("/users/list"))

        // verifico que a lista já tem um elemento
        mvcResult = mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk)
                .andReturn()

        result = mvcResult.modelAndView?.model?.get("users") as List<User>
        assertNotNull(result)
        assertEquals(1, result.size)
    }

}