package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.service.ProfessorsService
import com.github.fnunezkanut.util.randomString
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProfessorsFetchTest {

    private val professorsService = mockk<ProfessorsService>()

    //under test
    private val controller = ProfessorsFetch(
        professorsService = professorsService
    )


    @Test
    fun `main(), normal run, 200 on success`() {

        //given
        val uid = "7ec08266-ca4c-46a6-b8b0-d48e54eaafc7"
        val professor = Professor(
            uid = uid,
            code = ('0'..'9').randomString(7),
            firstName = "Richard",
            lastName = "Feynman"
        )

        every { professorsService.fetch(uid) } returns professor

        //when
        val actual = controller.main(
            professorUid = uid
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(200)
        assertThat(actual.body?.uid).isEqualTo(uid)
        assertThat(actual.body?.lastName).isEqualTo("Feynman")
    }
}