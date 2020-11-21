package com.github.fnunezkanut.repository


import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.util.randomString
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentsPostgresRepoTest {

    private val dbTool = PostgresTool()

    @AfterAll
    fun afterAll() {
        dbTool.stop()
    }

    @Test
    fun `studentsSimulation1`() {

        //under test
        val repo = StudentsPostgresRepo(
            jdbc = dbTool.jdbcTemplate
        )


        //create 1 student
        val studentCode = ('0'..'9').randomString(8)
        val result1 = repo.add(
            student = Student(
                firstName = ('a'..'z').randomString(10),
                lastName = ('a'..'z').randomString(12),
                code = studentCode
            )
        ) ?: fail("failed to add a student")
        assertThat(result1.code).isEqualTo(studentCode)

        //lookup the student we created by code
        val result2 = repo.fetchByCode(
            code = result1.code
        ) ?: fail("failed lookup a student")
        assertThat(result2.code).isEqualTo(studentCode)
    }
}