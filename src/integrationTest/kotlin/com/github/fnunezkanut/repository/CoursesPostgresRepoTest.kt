package com.github.fnunezkanut.repository


import com.github.fnunezkanut.model.Course
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoursesPostgresRepoTest {

    private val dbTool = PostgresTool()

    @AfterAll
    fun afterAll() {
        dbTool.stop()
    }

    @Test
    fun `create cource then look it up`() {

        //under test
        val repo = CoursesPostgresRepo(
            jdbc = dbTool.jdbcTemplate
        )


        //create 1 course
        val result1 = repo.add(
            course = Course(
                name = "Test 101",
                code = "TST-101"
            )
        ) ?: fail("failed to add a course")
        assertThat(result1.code).isEqualTo("TST-101")

        //lookup the course we created by code
        val result2 = repo.fetchByCode(
            code = result1.code
        ) ?: fail("failed lookup a course")
        assertThat(result2.code).isEqualTo("TST-101")
    }
}