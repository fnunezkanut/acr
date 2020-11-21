package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.service.CoursesService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CoursesCreate(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/v1/courses")
    fun main(
        @RequestBody createRequest: Course
    ): ResponseEntity<Course> {

        logger.info { "event: creating new course: $createRequest" }

        //validate incoming user data and create the course
        val validatedCourse = coursesService.validate(
            unvalidated = createRequest
        )
        val createdCourse = coursesService.create(
            validated = validatedCourse
        )

        return ResponseEntity(
            createdCourse,
            HttpStatus.CREATED //201
        )
    }
}