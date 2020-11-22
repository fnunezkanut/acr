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
class CoursesRegisterStudent(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/v1/courses/{courseUid}/students/{studentUid}")
    fun main(
        @PathVariable("courseUid") courseUid: String,
        @PathVariable("studentUid") studentUid: String
    ): ResponseEntity<MessageResponse> {

        logger.info { "event: registering a student $studentUid to a course: $courseUid" }

        coursesService.registerStudent(
            courseUid = courseUid,
            studentUid = studentUid
        )

        return ResponseEntity(
            MessageResponse("student registered to course"),
            HttpStatus.CREATED
        )
    }
}