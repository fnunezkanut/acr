package com.github.fnunezkanut.service


import com.github.fnunezkanut.expectApiError
import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.repository.CoursesPostgresRepo
import com.github.fnunezkanut.repository.ProfessorsPostgresRepo
import com.github.fnunezkanut.repository.StudentsPostgresRepo
import com.github.fnunezkanut.util.randomString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class CoursesServiceTest {

    private val coursesRepo = mockk<CoursesPostgresRepo>()
    private val professorsRepo = mockk<ProfessorsPostgresRepo>()
    private val studentsRepo = mockk<StudentsPostgresRepo>()

    //under test
    private val service = CoursesService(
        coursesRepo = coursesRepo,
        professorsRepo = professorsRepo,
        studentsRepo = studentsRepo
    )


    @Test
    fun `checkCode(), normal run`() {

        //given
        val code = "cs-101"
        val expected = "CS-101"

        every { coursesRepo.fetchByCode("CS-101") } returns null

        //when
        val actual = service.checkCode(_code = code)

        //then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `checkCode(), too long, 400 api error`() {

        //given
        val code = "this-code-is-too-long-yes-it-is"

        //when
        expectApiError(400) {
            service.checkCode(_code = code)
        }
    }

    @Test
    fun `checkCode(), entry already exists, 409 api error`() {

        //given
        val code = "cs-101"

        every { coursesRepo.fetchByCode("CS-101") } returns Course(
            code = "CS-101",
            name = "Computer Science 101"
        )

        //when
        expectApiError(409) {
            service.checkCode(_code = code)
        }
    }


    @Test
    fun `checkName(), normal run`() {

        //given
        val name = "Computer science with whitespace after       "
        val expected = "Computer science with whitespace after"

        //when
        val actual = service.checkName(_name = name)

        //then
        assertThat(actual).isEqualTo(expected)
    }


    @Test
    fun `validate(), normal run`() {

        //given
        val unvalidated = Course(
            code = "cs-101 ",
            name = " Computer Science 101 "
        )

        every { coursesRepo.fetchByCode("CS-101") } returns null

        //when
        val actual = service.validate(unvalidated = unvalidated)

        //then
        assertThat(actual.code).isEqualTo("CS-101")
        assertThat(actual.name).isEqualTo("Computer Science 101")
    }


    @Test
    fun `create(), normal run`() {

        //given
        val validated = Course(
            code = "CS-101",
            name = "Computer Science 101"
        )

        every {
            coursesRepo.add(validated)
        } returns validated.copy(
            uid = "b63da6a4-ceda-4958-9e39-0e608ad7021e"
        )

        //when
        val actual = service.create(validated = validated)

        //then
        assertThat(actual.code).isEqualTo("CS-101")
        assertThat(actual.name).isEqualTo("Computer Science 101")
        assertThat(actual.uid).isEqualTo("b63da6a4-ceda-4958-9e39-0e608ad7021e")

        verify(atLeast = 1) {
            coursesRepo.add(validated)
        }
    }


    @Test
    fun `assignProfessor(), normal run`() {

        //given
        val professorUid = UUID.randomUUID().toString()
        val courseUid = UUID.randomUUID().toString()

        every {
            coursesRepo.fetch(courseUid)
        } returns Course(
            uid = courseUid,
            code = "CS-101",
            name = "Computer Science 101"
        )

        every {
            professorsRepo.fetch(professorUid)
        } returns Professor(
            uid = professorUid,
            code = ('a'..'z').randomString(7),
            firstName = "John",
            lastName = "Doe"
        )

        every { coursesRepo.addProfessorRelation(courseUid, professorUid) } returns true

        //when
        service.assignProfessor(courseUid, professorUid)

        //then
        verify(atLeast = 1) {
            coursesRepo.addProfessorRelation(courseUid, professorUid)
        }
    }

    @Test
    fun `assignProfessor(), 404 no such course`() {

        //given
        val professorUid = UUID.randomUUID().toString()
        val courseUid = UUID.randomUUID().toString()

        every {
            coursesRepo.fetch(courseUid)
        } returns null

        //when, then
        expectApiError(404) {
            service.assignProfessor(courseUid, professorUid)
        }
    }


    @Test
    fun `assignProfessor(), 404 no such professor`() {

        //given
        val professorUid = UUID.randomUUID().toString()
        val courseUid = UUID.randomUUID().toString()

        every {
            coursesRepo.fetch(courseUid)
        } returns Course(
            uid = courseUid,
            code = "CS-101",
            name = "Computer Science 101"
        )

        every {
            professorsRepo.fetch(professorUid)
        } returns null

        //when, then
        expectApiError(404) {
            service.assignProfessor(courseUid, professorUid)
        }
    }


    @Test
    fun `registerStudent(), normal run`() {

        //given
        val studentUid = UUID.randomUUID().toString()
        val courseUid = UUID.randomUUID().toString()

        every {
            coursesRepo.fetch(courseUid)
        } returns Course(
            uid = courseUid,
            code = "CS-101",
            name = "Computer Science 101"
        )

        every {
            studentsRepo.fetch(studentUid)
        } returns Student(
            uid = studentUid,
            code = ('a'..'z').randomString(8),
            firstName = "John",
            lastName = "Doe"
        )

        every { coursesRepo.addStudentRelation(courseUid, studentUid) } returns true

        //when
        service.registerStudent(courseUid, studentUid)

        //then
        verify(atLeast = 1) {
            coursesRepo.addStudentRelation(courseUid, studentUid)
        }
    }

    @Test
    fun `registerStudent(), 404 no such course`() {

        //given
        val studentUid = UUID.randomUUID().toString()
        val courseUid = UUID.randomUUID().toString()

        every {
            coursesRepo.fetch(courseUid)
        } returns null

        //when, then
        expectApiError(404) {
            service.registerStudent(courseUid, studentUid)
        }
    }


    @Test
    fun `registerStudent(), 404 no such student`() {

        //given
        val studentUid = UUID.randomUUID().toString()
        val courseUid = UUID.randomUUID().toString()

        every {
            coursesRepo.fetch(courseUid)
        } returns Course(
            uid = courseUid,
            code = "CS-101",
            name = "Computer Science 101"
        )

        every {
            studentsRepo.fetch(studentUid)
        } returns null

        //when, then
        expectApiError(404) {
            service.registerStudent(courseUid, studentUid)
        }
    }
}