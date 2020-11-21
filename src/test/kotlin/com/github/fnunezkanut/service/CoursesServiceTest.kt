package com.github.fnunezkanut.service


import com.github.fnunezkanut.expectApiError
import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.repository.CoursesPostgresRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CoursesServiceTest {

    private val coursesRepo = mockk<CoursesPostgresRepo>()

    //under test
    private val service = CoursesService(
        coursesRepo = coursesRepo
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
}