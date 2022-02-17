package pt.ulusofona.tfc.trabalho.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations=["classpath:application-test.properties"])
@ActiveProfiles("test")
class TestUsersAPIController {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun testInsertAndThenList() {

        mockMvc.perform(post("/users-api/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "User 1", "age": 30 }"""))
                .andExpect(status().isCreated)

        mockMvc.perform(get("/users-api/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().json("""[{"id": 1, "name": "User 1", "age": 30 }]"""))
                .andReturn()

    }

}