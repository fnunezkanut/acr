package com.github.fnunezkanut.service


import com.github.fnunezkanut.expectApiError
import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.repository.StudentsPostgresRepo
import com.github.fnunezkanut.util.randomString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class StudentsServiceTest {

    private val studentsRepo = mockk<StudentsPostgresRepo>()

    //under test
    private val service = StudentsService(
        studentsRepo = studentsRepo
    )


    @Test
    fun `checkCode(), normal run`() {

        //given
        val code = "03344568"
        val expected = "03344568"

        every { studentsRepo.fetchByCode(code) } returns null

        //when
        val actual = service.checkCode(_code = code)

        //then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `checkCode(), too short, 400 api error`() {

        //given
        val code = "1234"

        //when
        expectApiError(400) {
            service.checkCode(_code = code)
        }
    }

    @Test
    fun `checkCode(), entry already exists, 409 api error`() {

        //given
        val code = "03344568"

        every { studentsRepo.fetchByCode("03344568") } returns Student(
            code = code,
            firstName = "firstname",
            lastName = "Lastname"
        )

        //when
        expectApiError(409) {
            service.checkCode(_code = code)
        }
    }


    @Test
    fun `checkName(), normal run`() {

        //given
        val name = "  John  "
        val expected = "John"

        //when
        val actual = service.checkName(_name = name)

        //then
        assertThat(actual).isEqualTo(expected)
    }


    @Test
    fun `validate(), normal run`() {

        //given
        val unvalidated = Student(
            code = "03344569",
            firstName = "Mary",
            lastName = "Poppins"
        )

        every { studentsRepo.fetchByCode("03344569") } returns null

        //when
        val actual = service.validate(unvalidated = unvalidated)

        //then
        assertThat(actual.code).isEqualTo("03344569")
        assertThat(actual.lastName).isEqualTo("Poppins")
    }


    @Test
    fun `create(), normal run`() {

        //given
        val validated = Student(
            code = "20012345",
            firstName = "Test",
            lastName = "Name"
        )

        every {
            studentsRepo.add(validated)
        } returns validated.copy(
            uid = "b63da6a4-ceda-4958-9e39-0e608ad7021e"
        )

        //when
        val actual = service.create(validated = validated)

        //then
        assertThat(actual.code).isEqualTo("20012345")
        assertThat(actual.firstName).isEqualTo("Test")
        assertThat(actual.uid).isEqualTo("b63da6a4-ceda-4958-9e39-0e608ad7021e")

        verify(atLeast = 1) {
            studentsRepo.add(validated)
        }
    }


    @Test
    fun `create(), db failure`() {

        //given
        val validated = Student(
            code = "20012345",
            firstName = "Test",
            lastName = "Name"
        )

        every {
            studentsRepo.add(validated)
        } returns null

        //when, then
        expectApiError(500) {
            service.create(validated = validated)
        }
    }


    @Test
    fun `fetch(), normal run`() {

        //given
        val student = Student(
            uid = UUID.randomUUID().toString(),
            code = ('a'..'z').randomString(8),
            firstName = "maggie",
            lastName = "simpsons"
        )

        every {
            studentsRepo.fetch(student.uid)
        } returns student

        //when
        val actual = service.fetch(uid = student.uid)

        //then
        assertThat(actual.firstName).isEqualTo("maggie")
        assertThat(actual.uid).isEqualTo(student.uid)
    }


    @Test
    fun `fetch(), nothing in db`() {

        //given
        val uid = UUID.randomUUID().toString()

        every {
            studentsRepo.fetch(uid)
        } returns null

        //when, then
        expectApiError(404) {
            service.fetch(uid = uid)
        }
    }
}