package com.github.fnunezkanut.service


import com.github.fnunezkanut.expectApiError
import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.repository.ProfessorsPostgresRepo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProfessorsServiceTest {

    private val professorsRepo = mockk<ProfessorsPostgresRepo>()

    //under test
    private val service = ProfessorsService(
        professorsRepo = professorsRepo
    )


    @Test
    fun `checkCode(), normal run`() {

        //given
        val code = "0011234"
        val expected = "0011234"

        every { professorsRepo.fetchByCode(code) } returns null

        //when
        val actual = service.checkCode(_code = code)

        //then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `checkCode(), wrong format, 400 api error`() {

        //given
        val code = "0011234ABC"

        //when
        expectApiError(400) {
            service.checkCode(_code = code)
        }
    }

    @Test
    fun `checkCode(), entry already exists, 409 api error`() {

        //given
        val code = "0011234"

        every { professorsRepo.fetchByCode("0011234") } returns Professor(
            code = code,
            firstName = "John",
            lastName = "Doe"
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
        val unvalidated = Professor(
            code = "0012222",
            firstName = " Jane",
            lastName = "Doe "
        )

        every { professorsRepo.fetchByCode("0012222") } returns null

        //when
        val actual = service.validate(unvalidated = unvalidated)

        //then
        assertThat(actual.code).isEqualTo("0012222")
        assertThat(actual.lastName).isEqualTo("Doe")
    }


    @Test
    fun `create(), normal run`() {

        //given
        val validated = Professor(
            code = "0012424",
            firstName = "Jack",
            lastName = "Bauer"
        )

        every {
            professorsRepo.add(validated)
        } returns validated.copy(
            uid = "b63da6a4-ceda-4958-9e39-0e608ad7021e"
        )

        //when
        val actual = service.create(validated = validated)

        //then
        assertThat(actual.code).isEqualTo("0012424")
        assertThat(actual.firstName).isEqualTo("Jack")
        assertThat(actual.uid).isEqualTo("b63da6a4-ceda-4958-9e39-0e608ad7021e")

        verify(atLeast = 1) {
            professorsRepo.add(validated)
        }
    }
}