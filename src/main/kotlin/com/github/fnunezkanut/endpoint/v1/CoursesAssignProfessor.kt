package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.MessageResponse
import com.github.fnunezkanut.service.CoursesService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CoursesAssignProfessor(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/v1/courses/{courseUid}/professors/{professorUid}")
    fun main(
        @PathVariable("courseUid") courseUid: String,
        @PathVariable("professorUid") professorUid: String
    ): ResponseEntity<MessageResponse> {

        logger.info { "event: assigning a professor $professorUid to a course: $courseUid" }

        //TODO check such a professor and/or course exists

        coursesService.assignProfessor(
            courseUid = courseUid,
            professorUid = professorUid
        )

        return ResponseEntity(
            MessageResponse("professor assigned to course"),
            HttpStatus.CREATED
        )
    }
}