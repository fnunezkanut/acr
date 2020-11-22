package com.github.fnunezkanut.repository


import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.util.randomString
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
    fun `coursesSimulation1`() {

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


    @Test
    fun `coursesSimulation2`() {

        //under test
        val coursesRepo = CoursesPostgresRepo(
            jdbc = dbTool.jdbcTemplate
        )
        val professorsRepo = ProfessorsPostgresRepo(
            jdbc = dbTool.jdbcTemplate
        )
        val studentsRepo = StudentsPostgresRepo(
            jdbc = dbTool.jdbcTemplate
        )


        //create 1 course, 1 student and 1 professor
        val courseResult = coursesRepo.add(
            course = Course(
                name = "Computer Graphics 2",
                code = "CG-201"
            )
        ) ?: fail("failed to add a course")

        coursesRepo.fetch(
            uid = courseResult.uid
        ) ?: fail("failed to lookup a course by uid:${courseResult.uid}")

        val professorCode = ('0'..'9').randomString(7)
        val professorResult = professorsRepo.add(
            professor = Professor(
                firstName = ('a'..'z').randomString(10),
                lastName = ('a'..'z').randomString(12),
                code = professorCode
            )
        ) ?: fail("failed to add a professor")

        professorsRepo.fetch(
            uid = professorResult.uid
        ) ?: fail("failed to lookup a professor by uid:${professorResult.uid}")

        val studentCode = ('0'..'9').randomString(8)
        val studentResult = studentsRepo.add(
            student = Student(
                firstName = ('a'..'z').randomString(10),
                lastName = ('a'..'z').randomString(12),
                code = studentCode
            )
        ) ?: fail("failed to add a student")

        studentsRepo.fetch(
            uid = studentResult.uid
        ) ?: fail("failed to lookup a student by uid:${studentResult.uid}")


        //great now that we have that setup, register the professor and student to the course
        val assignResult = coursesRepo.addProfessorRelation(
            courseUid = courseResult.uid,
            professorUid = professorResult.uid
        )
        assertThat(assignResult).isEqualTo(true)

        val registerResult = coursesRepo.addStudentRelation(
            courseUid = courseResult.uid,
            studentUid = studentResult.uid
        )
        assertThat(registerResult).isEqualTo(true)
    }
}