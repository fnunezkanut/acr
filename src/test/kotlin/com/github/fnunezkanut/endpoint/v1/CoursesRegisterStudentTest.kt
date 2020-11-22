package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.service.CoursesService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class CoursesRegisterStudentTest {

    private val coursesService = mockk<CoursesService>()

    //under test
    private val controller = CoursesRegisterStudent(
        coursesService = coursesService
    )


    @Test
    fun `main(), normal run, 201 on success`() {

        //given
        val courseUid = UUID.randomUUID().toString()
        val studentUid = UUID.randomUUID().toString()

        every { coursesService.registerStudent(courseUid, studentUid) } just runs

        //when
        val actual = controller.main(
            courseUid = courseUid,
            studentUid = studentUid
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(201)
        assertThat(actual.body?.message).isEqualTo("student registered to course")
    }
}