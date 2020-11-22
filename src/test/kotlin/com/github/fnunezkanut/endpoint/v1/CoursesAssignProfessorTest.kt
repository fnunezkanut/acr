package com.github.fnunezkanut.endpoint.v1


import com.github.fnunezkanut.service.CoursesService
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CoursesAssignProfessorTest {

    private val coursesService = mockk<CoursesService>()

    //under test
    private val controller = CoursesAssignProfessor(
        coursesService = coursesService
    )


    @Test
    fun `main(), normal run, 201 on success`() {

        //given
        val courseUid = "9da79cad-bcdf-4ae4-ab5a-b19693c9b5ee"
        val professorUid = "9e67e95d-e16d-40e9-b74b-9875c3fe7e35"

        every { coursesService.assignProfessor(courseUid, professorUid) } just runs

        //when
        val actual = controller.main(
            courseUid = courseUid,
            professorUid = professorUid
        )

        //then
        assertThat(actual.statusCodeValue).isEqualTo(201)
    }
}