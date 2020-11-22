package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.service.StudentsService
import com.github.fnunezkanut.util.randomString
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StudentsCreateTest {

    private val studentsService = mockk<StudentsService>()

    //under test
    private val controller = StudentsCreate(
        studentsService = studentsService
    )


    @Test
    fun `main(), normal run, 201 on success`() {

        //given
        val student = Student(
            code = ('0'..'9').randomString(8),
            firstName = "Bart",
            lastName = "Simpson"
        )

        every { studentsService.validate(any()) } returns student
        every { studentsService.create(student) } returns student.copy(
            uid = "43c32207-b7b1-4c79-8981-89105902d934"
        )

        //when
        val actual = controller.main(
            createRequest = student
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(201)
        assertThat(actual.body?.uid).isEqualTo("43c32207-b7b1-4c79-8981-89105902d934")
    }
}