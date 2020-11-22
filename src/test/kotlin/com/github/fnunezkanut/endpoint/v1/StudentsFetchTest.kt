package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.service.StudentsService
import com.github.fnunezkanut.util.randomString
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StudentsFetchTest {

    private val studentsService = mockk<StudentsService>()

    //under test
    private val controller = StudentsFetch(
        studentsService = studentsService
    )


    @Test
    fun `main(), normal run, 200 on success`() {

        //given
        val uid = "91c18b83-9dc9-40e2-a3cf-9de9aef4075f"
        val student = Student(
            uid = uid,
            code = ('0'..'9').randomString(8),
            firstName = "Lisa",
            lastName = "Simpson"
        )

        every { studentsService.fetch(uid) } returns student

        //when
        val actual = controller.main(
            studentUid = uid
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(200)
        assertThat(actual.body?.uid).isEqualTo(uid)
        assertThat(actual.body?.lastName).isEqualTo("Simpson")
    }
}