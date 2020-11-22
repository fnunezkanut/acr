package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.service.CoursesService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CoursesFetchTest {

    private val coursesService = mockk<CoursesService>()

    //under test
    private val controller = CoursesFetch(
        coursesService = coursesService
    )


    @Test
    fun `main(), normal run, 200 on success`() {

        //given
        val uid = "a8632959-de2b-4ce3-8e40-2febe4736483"
        val course = Course(
            uid = uid,
            code = "EE-130",
            name = "Fundamentals Electronic Eng"
        )

        every { coursesService.fetch(uid) } returns course

        //when
        val actual = controller.main(
            courseUid = uid
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(200)
        assertThat(actual.body?.uid).isEqualTo(uid)
    }
}