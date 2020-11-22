package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.service.ProfessorsService
import com.github.fnunezkanut.util.randomString
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProfessorsCreateTest {

    private val professorsService = mockk<ProfessorsService>()

    //under test
    private val controller = ProfessorsCreate(
        professorsService = professorsService
    )


    @Test
    fun `main(), normal run, 201 on success`() {

        //given
        val professor = Professor(
            code = ('0'..'9').randomString(7),
            firstName = "Richard",
            lastName = "Feynman"
        )

        every { professorsService.validate(any()) } returns professor
        every { professorsService.create(professor) } returns professor.copy(
            uid = "dd60fba9-3ad1-4faa-bc11-7f84dab0fa16"
        )

        //when
        val actual = controller.main(
            createRequest = professor
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(201)
        assertThat(actual.body?.uid).isEqualTo("dd60fba9-3ad1-4faa-bc11-7f84dab0fa16")
    }
}