package com.github.fnunezkanut.repository


import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.util.randomString
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfessorsPostgresRepoTest {

    private val dbTool = PostgresTool()

    @AfterAll
    fun afterAll() {
        dbTool.stop()
    }

    @Test
    fun `professorsSimulation1`() {

        //under test
        val repo = ProfessorsPostgresRepo(
            jdbc = dbTool.jdbcTemplate
        )


        //create 1 professor
        val professorCode = ('0'..'9').randomString(7)
        val result1 = repo.add(
            professor = Professor(
                firstName = ('a'..'z').randomString(10),
                lastName = ('a'..'z').randomString(12),
                code = professorCode
            )
        ) ?: fail("failed to add a professor")
        assertThat(result1.code).isEqualTo(professorCode)

        //lookup the professor we created by code
        val result2 = repo.fetchByCode(
            code = result1.code
        ) ?: fail("failed lookup a professor")
        assertThat(result2.code).isEqualTo(professorCode)
    }
}