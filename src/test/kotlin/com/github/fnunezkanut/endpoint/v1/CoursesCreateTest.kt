package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.service.CoursesService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CoursesCreateTest {

    private val coursesService = mockk<CoursesService>()

    //under test
    private val controller = CoursesCreate(
        coursesService = coursesService
    )


    @Test
    fun `main(), normal run, 201 on success`() {

        //given
        val course = Course(
            code = "EE-130",
            name = "Fundamentals Electronic Eng"
        )

        every { coursesService.validate(any()) } returns course
        every { coursesService.create(course) } returns course.copy(
            uid = "a8632959-de2b-4ce3-8e40-2febe4736483"
        )

        //when
        val actual = controller.main(
            createRequest = course
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(201)
        assertThat(actual.body?.uid).isEqualTo("a8632959-de2b-4ce3-8e40-2febe4736483")
    }
}